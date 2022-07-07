const contents = $('#content');

toastui.Editor.setLanguage(['ko', 'ko-KR'], {
    Markdown: '마크다운',
    WYSIWYG: '일반',
    Write: '편집하기',
    Preview: '미리보기',
    Headings: '제목크기',
    Paragraph: '본문',
    Bold: '굵게',
    Italic: '기울임꼴',
    Strike: '취소선',
    Code: '인라인 코드',
    Line: '문단나눔',
    Blockquote: '인용구',
    'Unordered list': '글머리 기호',
    'Ordered list': '번호 매기기',
    Task: '체크박스',
    Indent: '들여쓰기',
    Outdent: '내어쓰기',
    'Insert link': '링크 삽입',
    'Insert CodeBlock': '코드블럭 삽입',
    'Insert table': '표 삽입',
    'Insert image': '이미지 삽입',
    Heading: '제목',
    'Image URL': '이미지 주소',
    'Select image file': '이미지 파일을 선택하세요.',
    'Choose a file': '파일 선택',
    'No file': '선택된 파일 없음',
    Description: '설명',
    OK: '확인',
    More: '더 보기',
    Cancel: '취소',
    File: '파일',
    URL: '주소',
    'Link text': '링크 텍스트',
    'Add row to up': '위에 행 추가',
    'Add row to down': '아래에 행 추가',
    'Add column to left': '왼쪽에 열 추가',
    'Add column to right': '오른쪽에 열 추가',
    'Remove row': '행 삭제',
    'Remove column': '열 삭제',
    'Align column to left': '열 왼쪽 정렬',
    'Align column to center': '열 가운데 정렬',
    'Align column to right': '열 오른쪽 정렬',
    'Remove table': '표 삭제',
    'Would you like to paste as table?': '표형태로 붙여 넣겠습니까?',
    'Text color': '글자 색상',
    'Auto scroll enabled': '자동 스크롤 켜짐',
    'Auto scroll disabled': '자동 스크롤 꺼짐',
    'Choose language': '언어 선택',
})

const editor = new toastui.Editor({
    el: document.querySelector('#editor'),
    height: '800px',
    initialEditType: 'markdown',
    previewStyle: 'vertical',
    language: 'ko',
    toolbarItems: [
        ['heading', 'bold', 'italic', 'strike'],
        ['hr', 'quote'],
        ['ul', 'ol', 'task', 'indent', 'outdent'],
        ['table', 'image', 'link'],
        ['code', 'codeblock'],
        ['scrollSync'],
    ],
    hooks: {
        addImageBlobHook: (blob, callback) => {
            let imgurl = articleIndex.uploadImage(blob);
            callback(imgurl, "첨부 이미지")
        }
    }
});

let articleIndex = {
    init: function () {
        let _this = this;
        $('#article-write').on("click", function () {
            _this.writeArticle();
        });
        $('#article-modify').on("click", function () {
            console.log($(this).attr("value"));
            _this.modifyArticle($(this).attr("value"));
        });
        $(".article-delete").on("click", function () {
            console.log("hello");
            _this.deleteArticle($(this).attr("value"));
        });

    },
    uploadImage: function(blob) {
        let url = '';
        let token = getCsrfToken();
        let formData = new FormData();
        formData.append('file', blob);

        $.ajax({
            type: 'POST',
            url: '/api/v1/admin/file/upload',
            headers: {'X-XSRF-TOKEN': token},
            processData: false,
            contentType: false,
            async: false,
            data: formData
        }).done(function (result) {
            url = result;
        }).fail(function (error) {
            alert(error.responseJSON.message);
        });

        return url;
    },

    writeArticle: function () {
        contents.val(editor.getMarkdown());
        if (!checkTitle()) {
            alert("제목을 입력해주세요")
            return;
        } else if (!checkContent()) {
            alert("내용은 65000자 이하로 작성하거나 내용을 입력해주세요.")
            return;
        } else if (!checkCategory()) {
            alert("카테고리를 입력해주세요")
            return;
        } else if (!checkTags()) {
            alert("태그를 입력해주세요")
            return;
        }
        let token = getCsrfToken();
        let data = {
            thumbnailUrl: $("#thumbnailUrl").val(),
            category: $("#category").val(),
            title: $("#title").val(),
            content: contents.val(),
            tags: $("#tags").val(),
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/admin/article',
            headers: {'X-XSRF-TOKEN': token},
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (res) {
            autoSaveArticleIndex.deleteAutoSavedArticle();
            window.location.href = "/admin/article?categoryTitle=ALL";
        }).fail(function (error) {
            if (error.responseJSON.code == 701 || error.responseJSON.code == 702) {
                alert(error.responseJSON.message);
                window.location.href = "/login";
            }
            if (error.responseJSON.code == 400) {
                alert(error.responseJSON.message[0]);
            }
            if (error.responseJSON.code == 901) {
                alert(error.responseJSON.message)
            }
        });
    },
    modifyArticle: function (articleId) {
        contents.val(editor.getMarkdown());
        if (!checkTitle()) {
            alert("제목을 입력해주세요")
            return;
        } else if (!checkContent()) {
            alert("내용은 65000자 이하로 작성하거나 내용을 입력해주세요.")
            return;
        } else if (!checkCategory()) {
            alert("카테고리를 선택해주세요")
            return;
        } else if (!checkTags()) {
            alert("태그를 입력해주세요")
            return;
        }
        console.log($("#thumbnailUrl").val());
        let token = getCsrfToken();
        let data = {
            thumbnailUrl: $("#thumbnailUrl").val(),
            category: $("#category").val(),
            title: $("#title").val(),
            content: contents.val(),
            tags: $("#tags").val(),
        };
        console.log(articleId);
        $.ajax({
            type: 'PATCH',
            url: '/api/v1/admin/article/' + articleId,
            headers: {'X-XSRF-TOKEN': token},
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            autoSaveArticleIndex.deleteAutoSavedArticle();
            window.location.href = "/article/" + articleId;
        }).fail(function (error) {
            if (error.responseJSON.code == 701 || error.responseJSON.code == 702) {
                alert(error.responseJSON.message);
                window.location.href = "/login";
            }
            if (error.responseJSON.code == 400) {
                alert(error.responseJSON.message[0]);
            }
        });
    },
    deleteArticle: function (articleId) {
        // 나중에 confirm 으로 변경하자
        if (!confirm("해당 글을 삭제 하시겠습니까?")) {
            return;
        }
        let token = getCsrfToken();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/admin/article/' + articleId,
            headers: {'X-XSRF-TOKEN': token},
            contentType: 'application/json; charset=utf-8',
        }).done(function (res) {
            window.location.href = "/admin/article?categoryTitle=ALL"
        }).fail(function (error) {
            console.log(error);
            alert("에러");
        });
    },


}

articleIndex.init();

// 유효성 검사
function checkTitle() {
    return $('#title').val() !== "";
}

function checkContent() {
    return contents.val().length <=  65535 && contents.val() !=="";


}

function checkCategory() {
    return $('#category').val() !== "카테고리를 선택해주세요";
}

function checkTags() {
    return $('#tags').val() !== "";
}

