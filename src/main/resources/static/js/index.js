// 文件分片上传
function fileUpload() {
  const file = $("#file")[0].files[0];
  upload(file, 0);
}

const upload = function (file, num) {

  //每一块大小
  let blockSize = 1024 * 1000 * 100;
  //总块数
  let blockNum = Math.ceil(file.size / blockSize);
  // 不够分块大小 取全部大小
  let nextSize = Math.min((num + 1) * blockSize, file.size);

  let fileblock = file.slice(num * blockSize, nextSize);

  const formData = new FormData();
  formData.append("file", fileblock);
  formData.append("fileName", file.name);
  formData.append("blockNum", blockNum);
  formData.append("blockIndex", (num + 1));
  $.ajax({
    url: "/upload",
    type: "POST",
    data: formData,
    //不要去处理发送的数据
    processData: false,
    // 不要去设置Content-Type请求头
    contentType: false,
    beforeSend() {
      console.log("beforeSend")
    },
    success: function (responseText) {
      $(".message").html("上传进度--" + (num + 1) + "/" + blockNum);
      if (file.size <= nextSize) {
        $(".result").html("上传完成!");
        return;
      }
      upload(file, ++num);//递归调用
    }
  });

}

// 选中文件夹上传
function uploadFiles() {
  const files = $('#files')[0].files;
  let formData = new FormData();
  for (let index in files) {
    formData.append('files', files[index]);
  }

  $.ajax({
    url: "/UploadFileDir",
    type: "POST",
    data: formData,
    //不要去处理发送的数据
    processData: false,
    // 不要去设置Content-Type请求头
    contentType: false,
    beforeSend() {

    },
    success: function (responseText) {
      $('.message1').text("后台返回:\n" + responseText);
    }
  });
}