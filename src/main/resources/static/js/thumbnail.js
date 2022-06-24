
const thumbBox = document.getElementById("thumbBox");
const uploadThumbBtn = document.getElementById("thumbnail");
const thumbDel = document.getElementById("thumbDelBtn");
const previewThumb = document.getElementById("thumbnailPreView");
const thumbUrl = document.getElementById("thumbnailUrl");
const thumbUrlUploadBtn = document.getElementById("thumbnail-url-upload-btn");

function uploadImg(input) {

    if(input.files && input.files[0]) {

        let token = getCsrfToken();
        let formData = new FormData();
        formData.append('file', input.files[0]);

        const xhr = new XMLHttpRequest();
        xhr.open("POST", "/file/upload", false);
        xhr.setRequestHeader("contentType", "multipart/form-data");
        xhr.setRequestHeader("X-XSRF-TOKEN", token);
        xhr.send(formData);

        if (xhr.readyState === 4 && xhr.status === 200) {

            thumbUrl.value = xhr.response;
            previewThumb.src = thumbUrl.value;

            thumbBox.style.display = ''

        } else {
            alert("이미지가 정상적으로 업로드되지 못했습니다.")
        }

    }
}



thumbUrlUploadBtn.addEventListener("click", () =>{
    const thumbUrlUploadInput = document.getElementById("thumbnail-url-upload-input");
    const url = thumbUrlUploadInput.value;
    previewThumb.src = url;
    thumbUrl.value = url;
    thumbBox.style.display = '';
})

uploadThumbBtn.addEventListener("change", e => {
    uploadImg(e.target);
})

thumbDel.addEventListener("click", () =>{
    thumbBox.style.display = 'none';
    thumbUrl.value = "";
})