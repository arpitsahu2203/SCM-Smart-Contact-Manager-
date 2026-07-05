console.log("admin user");

document.querySelector("#imageFileInput").addEventListener('change',function (event){

    let file=event.target.files[0];
    let reader=new FileReader();
    reader.onload=function(){
        document.getElementById("uplaod_image_preview").src=reader.result;
    };
    reader.readAsDataURL(file);
});