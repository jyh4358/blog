let shareIndex = {
    init: function () {
        let _this = this;
        $('#kakao-share').on("click", function () {
            _this.shareKakao();
        });
        $('#naver-share').on("click", function () {
            _this.shareNaver();
        });
        $('#facebook-share').on("click", function () {
            _this.shareFacebook();
        });
    },
    shareKakao: function () {
        console.log($('#article-title').text());
        console.log($('#article-title').val());
        console.log($('#article-content').text());
        console.log($('#article-content').val());

        Kakao.init('144c4366d6dbf3169f57324d247edb1b');

        Kakao.Link.sendDefault({
            objectType: 'feed',
            content: {
                title: $('#article-title').text() + " - JDong's LOG",
                description: $('#article-content').text(),
                imageUrl: $('#article-thumb').attr("src"),
                link: {
                    mobileWebUrl: window.location.href,
                    webUrl: window.location.href,
                },
            },
            buttons: [
                {
                    title: "JDong's blog에서 보기",
                    link: {
                        mobileWebUrl: window.location.href ,
                        webUrl: window.location.href,
                    },
                }
            ],
        });
    },
    shareNaver: function () {
        let url = encodeURIComponent(window.location.href);
        let title = encodeURIComponent($('#article-title').text() + " - 제이동의 블로그");
        let link = "https://share.naver.com/web/shareView.nhn?url=" + url + "&title=" + title;
        window.open( link, 'share', 'width=500, height=500' );
    },
    shareFacebook: function () {
        window.open( 'http://www.facebook.com/sharer.php?u=' + encodeURIComponent(window.location.href));
    },
};
shareIndex.init();