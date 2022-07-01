let autoSaveArticleIndex = {
    init: function () {
        let _this = this;
        setInterval("autoSaveArticleIndex.autoSave()",10000);
        _this.getAutoSavedArticle();
    },
    autoSave: function () {
        contents.val(editor.getMarkdown());
        console.log("autoSave 호출")
        if (!checkTitle()) {
            console.log("확인 체크")
            return;
        }
        else if (!checkContent()) {
            console.log("확인 content 체크")
            return;
        }

        let token = getCsrfToken();
        let autoSaveArticle = {
            title: $('#title').val(),
            content: editor.getMarkdown(),
            thumbnailUrl: $('#thumbnailUrl').val(),
            category: $('#category').val(),
            tag: $('#tags').val(),
        }
        $.ajax({
            type: 'POST',
            url: '/api/v1/admin/article-auto',
            headers: {'X-XSRF-TOKEN': token},
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(autoSaveArticle)
        }).done(function () {
            console.log("저장 성공")
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    getAutoSavedArticle: function () {

        $.ajax({
            type: 'GET',
            url: '/api/v1/admin/article-auto',
            contentType: 'application/json; charset=utf-8',
        }).done(function (res) {
            if (res.title) {
                if (confirm("저장된 글이 있습니다. 이어서 작성하시겠습니까?")) {
                    $('#title').val(res.title);
                    editor.setMarkdown(res.content);
                    $('#thumbnailUrl').val(res.thumbnailUrl);
                    $('#tags').val(res.tag);
                    $('#category').val(res.category).attr("selected", "selected");
                }
            }
            console.log(res);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    deleteAutoSavedArticle: function () {
        let token = getCsrfToken();
        $.ajax({
            type: 'DELETE',
            url: '/api/v1/admin/article-auto/' + 1,
            headers: {'X-XSRF-TOKEN': token},
            contentType: 'application/json; charset=utf-8',
        }).done(function () {
            console.log("삭제 성공");
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
}
autoSaveArticleIndex.init();