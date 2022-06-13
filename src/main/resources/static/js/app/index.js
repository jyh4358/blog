let index = {
    init: function () {
        let _this = this;
        $("#btn-oauth").on("click", function () {
            _this.save()
        });
    },
    save: function () {
        $.ajax({
            type: 'GET',
            url: '/google/login'
        }).done(function (result) {
            console.log(result)
            alert('성공');
            location.href = result;
        }).fail(function (error) {
            alert('에러');
        });
    },

}
index.init();