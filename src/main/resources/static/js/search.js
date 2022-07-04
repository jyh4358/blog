let searchIndex = {
    init: function () {
        $('.searchInput').on("keyup", function (key) {
            if (key.keyCode == 13) {
                location.href = "/article-search?keyword=" +$(this).val();
            }
        });
    },
}
searchIndex.init();