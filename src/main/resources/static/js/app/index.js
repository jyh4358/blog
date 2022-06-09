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
            headers: {'Access-Control-Allow-Origin': '*'},
            url: 'https://accounts.google.com/o/oauth2/v2/auth?client_id=428541390243-7cevccqe0afejrec8et1025hbk8v36p0.apps.googleusercontent.com&amp;response_type=code&amp;scope=email%20profile&amp;redirect_uri=http://localhost:8080/login/test'
        }).done(function (result) {
            alert('성공');
        }).fail(function (error) {
            alert('에러');
        });
    },

}
index.init();