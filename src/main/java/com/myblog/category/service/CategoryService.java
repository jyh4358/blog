package com.myblog.category.service;

import com.myblog.category.dto.CategoryListDto;
import com.myblog.category.dto.CategoryQueryDto;
import com.myblog.category.dto.ChildCategoryList;
import com.myblog.category.dto.ParentCategoryList;
import com.myblog.category.model.Category;
import com.myblog.category.resposiotry.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryListDto findCategories() {
        List<Category> findCategories = categoryRepository.findAll();
        List<Category> parentCategories = findCategories.stream().filter(s -> s.getParent() == null)
                .collect(Collectors.toList());

        List<ParentCategoryList> parentCategoryLists = parentCategories.stream().map(
                s -> ParentCategoryList.of(s)
        ).collect(Collectors.toList());

        return CategoryListDto.of(parentCategoryLists);
    }

    @Transactional
    public void editCategory(CategoryListDto categoryListDto) {

        List<Category> categories = categoryRepository.findAll();
        List<ParentCategoryList> parentCategoryLists = categoryListDto.getParentCategoryLists();

        for (ParentCategoryList parentCategoryList : parentCategoryLists) {
            modifyCategoryTitle(parentCategoryList, categories);
            createNewCategory(parentCategoryList, categories);
            deleteCategory(parentCategoryList, categories);

        }
    }


    public CategoryListDto findSidebarCategory() {
        CategoryListDto categoryListDto = findCategories();
        List<CategoryQueryDto> categoryCounts = categoryRepository.findCategoryCount();
        for (CategoryQueryDto categoryCount : categoryCounts) {
            categoryListDto.getParentCategoryLists().forEach(
                    s -> {
                        if (s.getId().equals(categoryCount.getId())) {
                            s.setCount((categoryCount.getCount().intValue()));
                        } else {
                            insertChildCategoryCount(s, categoryCount);
                        }
                    }
            );
        }

        parentCategoryTotalCalc(categoryListDto);
        categoryTotalCalc(categoryListDto);


        return categoryListDto;
    }



    // 서비스 로직
    private void insertChildCategoryCount(ParentCategoryList parentCategoryList, CategoryQueryDto categoryCount) {
        for (ChildCategoryList childCategoryList : parentCategoryList.getChildCategoryLists()) {
            if (childCategoryList.getId().equals(categoryCount.getId())) {
                childCategoryList.setCount(categoryCount.getCount().intValue());
            }
        }
    }

    public void modifyCategoryTitle(ParentCategoryList parentCategoryList, List<Category> categories) {
        categories.forEach(s ->
        {
            if (s.getId() == parentCategoryList.getId()) {
                s.changeTitle(parentCategoryList.getParentCategory());
                List<ChildCategoryList> collect =
                        parentCategoryList.getChildCategoryLists().stream().filter(f -> !(f.checkNewCategory()))
                                .collect(Collectors.toList());

//                이 방법은 영속 객체를 없애고 새로영속 객체를 넣을려는 시도를 하여 cascade쪽 문제가 발생
//                List<Category> children = collect.stream().map(t ->
//                        Category.createCategory(t.getId(), t.getChildCategory(), s)).collect(Collectors.toList()
//                );
//                s.setChild(children);

                collect.forEach(t ->
                        modifyChildCategoryTitle(t, s.getChild()));
            }
        });
    }

    public void modifyChildCategoryTitle(ChildCategoryList childCategoryList, List<Category> categories) {
        for (Category category : categories) {
            if (category.getId().equals(childCategoryList.getId())) {
                category.changeTitle(childCategoryList.getChildCategory());
            }
        }
    }

    public void createNewCategory(ParentCategoryList parentCategoryList, List<Category> categories) {
        if (parentCategoryList.checkNewCategory()) {
            categoryRepository.save(new Category(parentCategoryList.getParentCategory()));
        }
        for (Category category : categories) {
            if (parentCategoryList.getId() == category.getId()) {
                List<ChildCategoryList> collect = parentCategoryList.getChildCategoryLists().stream().filter(f -> f.checkNewCategory())
                        .collect(Collectors.toList());
                collect.stream().forEach(s ->
                        categoryRepository.save(Category.createCategory(null, s.getChildCategory(), category))
                );
//                for (ChildCategoryList childCategoryList : collect) {
//                    categoryRepository.save(new Category(childCategoryList.getChildCategory(), category));
//                }
            }
        }
    }

    public void deleteCategory(ParentCategoryList parentCategoryList, List<Category> categories) {
        if (parentCategoryList.getDeleteCheck()) {
            categories.forEach(s ->
            {
                if (s.getId().equals(parentCategoryList.getId())) {
                    categoryRepository.delete(s);
                }
            });
        } else {
            List<ChildCategoryList> deleteChildCategories = parentCategoryList.getChildCategoryLists().stream().filter(s -> s.getDeleteCheck()).collect(Collectors.toList());
            categories.forEach(s ->
            {
                if (s.getId().equals(parentCategoryList.getId())) {
                    deleteChildCategories.forEach(t ->
                            deleteChildCategoryTitle(t, s.getChild()));
                    System.out.println("s.getChild().size() = " + s.getChild().size());
                }
            });

        }

    }

    private void deleteChildCategoryTitle(ChildCategoryList childCategoryList, List<Category> child) {
        for (Iterator<Category> it = child.iterator() ; it.hasNext() ;) {
            Category category = it.next();
            if (category.getId().equals(childCategoryList.getId())) {
                it.remove();
                categoryRepository.delete(category);
            }
        }

    }

    private void parentCategoryTotalCalc(CategoryListDto categoryListDto) {
        for (ParentCategoryList parentCategoryList : categoryListDto.getParentCategoryLists()) {
            parentCategoryList.getChildCategoryLists().forEach(s -> parentCategoryList.addParentCount(s.getCount()));
        }
    }

    private void categoryTotalCalc(CategoryListDto categoryListDto) {
        for (ParentCategoryList parentCategoryList : categoryListDto.getParentCategoryLists()) {
            categoryListDto.addTotalCount(parentCategoryList.getCount());
        }
    }


}
