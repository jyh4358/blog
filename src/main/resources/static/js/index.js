$(document).ready(function () {
    mainIndex.init();
    $('.post-wrapper').slick({
        slidesToShow: 3,
        slidesToScroll: 1,
        autoplay: true,
        dots: false,
        autoplaySpeed: 3000,
        nextArrow: $('.next'),
        prevArrow: $('.prev'),
        responsive: [
            {
                breakpoint: 1024,
                settings: {
                    slidesToShow: 3,
                    slidesToScroll: 3,
                    infinite: true,
                }
            },
            {
                breakpoint: 600,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 2
                }
            },
            {
                breakpoint: 480,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1
                }
            }
            // You can unslick at a given breakpoint now by adding:
            // settings: "unslick"
            // instead of a settings object
        ]
    });
});

let mainIndex ={
    data : {
        curPage: 0,
        dataCheck: true,
    },
    init: function () {
        let _this = this;
        _this.scrollEvent();

    },
    scrollEvent: function () {
        // 스크롤이 움직일때마다 호출되는 이벤트
        $(window).scroll(function () {
            if (($(window).scrollTop() == $(document).height() - $(window).height() - 200) && mainIndex.data.dataCheck) {
                mainIndex.getArticleList();
                mainIndex.data.curPage++;
            }
        });

    },
    getArticleList: function () {
        $.ajax({
            type: 'GET',
            url: '/api/v1/article' + '?curPage=' + this.data.curPage,
            contentType: 'application/json; charset=utf-8',
        }).done(function (popularArticleResponse) {
            if (popularArticleResponse.length == 0) {
                mainIndex.data.dataCheck = false;
            }
            mainIndex.makeNextPage(popularArticleResponse);
        }).fail(function (error) {
            console.log(error);
            alert("데이터를 가져오지 못했습니다.");
        });
    },
    makeNextPage: function (popularArticleResponse) {

        let articleHtmlSource = ' ';
        for (const popularArticleResponseElement of popularArticleResponse) {
            articleHtmlSource +=
                `<div class=\"card mb-3 recent-card wow fadeInUp ">
                        <a href="/article/${popularArticleResponseElement.id}">
                            <div class="row g-0">
                                <div class="col-1 mb-3 align-self-center">
                                    <h4 class="text-center">${popularArticleResponseElement.id}</h4>
                                </div>
                                <div class="col-3">
                                    <div class="ratio ratio-1x1\" style="background-image: url(${popularArticleResponseElement.thumbnailUrl}); background-position: center; background-size: cover;"></div>
                                </div>
                                <div class="col-8 row row-cols-1 align-self-center">
                                    <h3 class="card-title col mb-3 text-truncate">${popularArticleResponseElement.title}</h3>
                                    <p class="d-none d-md-block col recent-card-text">${popularArticleResponseElement.content}</p>
                                    <p class="col mb-0"><small class="text-muted">${popularArticleResponseElement.createDate.split('T')[0]}</small></p>
                                </div>
                            </div>
                        </a>
                    </div>`
        }
        $('#autoScroll').append(articleHtmlSource);
    },
}