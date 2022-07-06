let thumbnailIndex = {
    init: function () {
        let _this = this;
        $("#thumbnail").on("change", function () {
            _this.uploadThumbnail();
        });
        $("#thumbnail-url-upload-btn").on("click", function () {
            _this.uploadThumbnailUrl();
        });
        $("#thumbDelBtn").on("click", function () {
            _this.deleteThumbnail();
        });

    },
    uploadThumbnail: function () {
        let token = getCsrfToken();
        let formData = new FormData();
        formData.append('file', $("#thumbnail")[0].files[0]);

        $.ajax({
            type: 'POST',
            url: '/api/v1/admin/file/upload',
            headers: {'X-XSRF-TOKEN': token},
            processData: false,
            contentType: false,
            async: false,
            data: formData
        }).done(function (result) {
            $("#thumbnailUrl").val(result);
            $("#thumbnailPreView").attr("src", result);
            $("#thumbBox").css("display", "");
            $('#thumbnailModal').modal('hide');
        }).fail(function (error) {
            if (error.responseJSON.code == 701 || error.responseJSON.code == 702) {
                alert(error.responseJSON.message);
                window.location.href = "/login";
            }
            if (error.responseJSON.code == 801) {
                alert(error.responseJSON.message);
            }
        });
    },
    uploadThumbnailUrl: function () {
        let thumbnailUrl = $("#thumbnail-url-upload-input").val();
        $("#thumbnailUrl").val(thumbnailUrl)
        $("#thumbnailPreView").attr("src", thumbnailUrl);
        $("#thumbBox").css("display", "");
        $('#thumbnailModal').modal('hide');
    },
    deleteThumbnail: function () {
        $("#thumbBox").css("display", "none");
        $("#thumbnailUrl").val("");
    },
}
thumbnailIndex.init();
