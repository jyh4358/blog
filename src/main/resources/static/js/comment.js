let commentIndex = {
    init: function () {
        let _this = this;
        $("#comment-save").on("click", function () {
            let attr = $(this).attr("id");
            _this.commentSave();
        });
        $("#child-comment-save").on("click", function () {
            _this.commentSave($(this).attr("value"));
        });
        $(".comment-delete").on("click", function () {
            _this.commentDelete($(this).attr("value"));
        });


    },
    commentSave: function (parentCommentId) {
        let token = getCsrfToken();
        let data ={};
        console.log("==============");
        alert(!parentCommentId);
        if (!parentCommentId) {
            data = {
                articleId: $("#article-id").attr("value"),
                content: $("#comment-content").val(),
                secret: $("#comment-secret").is(":checked"),
                parentCommentId: parentCommentId,
            };
            if(!data.content){
                alert("댓글을 작성해주세요");
                $('#parent-content-error').text('댓글을 작성해주세요')
                return;
            }
        } else {
            data = {
                articleId: $("#article-id").val(),
                content: $("#child-comment-content").val(),
                secret: $("#child-comment-secret").is(":checked"),
                parentCommentId: parentCommentId,
            };
            if(!data.content){
                alert("댓글을 작성해주세요");
                $('#child-content-error').text('댓글을 작성해주세요')
                return;
            }
        }

        $.ajax({
            type: 'POST',
            url: '/api/v1/comment',
            headers: {'X-XSRF-TOKEN': token},
            contentType: 'application/json; charset=utf-8',
            async: false,
            data: JSON.stringify(data)
        }).done(function (res) {
            window.location.reload();
        }).fail(function (error) {
            console.log(error);
            alert("에러");
        });

    },
    commentDelete: function (commentId) {
        alert("해당 댓글을 삭제하시겠습니까?");
        let token = getCsrfToken();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/comment/' + commentId,
            headers: {'X-XSRF-TOKEN': token},
            contentType: 'application/json; charset=utf-8',
        }).done(function (res) {
            window.location.reload();
        }).fail(function (error) {
            console.log(error);
            alert("에러");
        });
    },


}
commentIndex.init();