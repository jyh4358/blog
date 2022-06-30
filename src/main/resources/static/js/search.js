let searchIndex = {
    init: function () {
        $('#searchInput').on("keyup", function (key) {
            if (key.keyCode == 13) {
                console.log($(this).val());
                location.href = "/article-search/?keyword=" +$(this).val();
            }
        });
    },
}
searchIndex.init();