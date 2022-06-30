
// 자동 저장 기능
function autoSave() {

    let token = getCsrfToken();
    let tempDto = {};
    tempDto.content = editor.getMarkdown();

    const xhr = new XMLHttpRequest();
    xhr.open("POST", "/article/temp/autoSave")
    xhr.setRequestHeader("content-type", "application/json");
    xhr.setRequestHeader("X-XSRF-TOKEN", token);
    xhr.send(JSON.stringify(tempDto));

    xhr.onload = () => {

        if (xhr.status === 200 || xhr.status === 201 || xhr.status === 202) {

            console.log("autoSave");

        }
    }

}

setInterval("autoSave()", 60000);

window.onload = function () {

    const xhr = new XMLHttpRequest();
    xhr.open("GET", "/article/temp/getTemp")
    xhr.send();

    xhr.onload = () => {
        if (xhr.status === 200 || xhr.status === 201 || xhr.status === 202) {

            let tmp = JSON.parse(xhr.response);

            if (tmp.content != null && tmp.content !== '') {
                let isLoadTmp = confirm("이전에 작성하던 글이 있습니다. 이어서 작성하시겠습니까?");
                if (isLoadTmp) {
                    editor.setMarkdown(tmp.content);
                }
            }
        }
    }
}

