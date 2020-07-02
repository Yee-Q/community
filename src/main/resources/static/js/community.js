function post() {
    let tid = $("#tid").val();
    let content = $("#comment-content").val();
    $.ajax({
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        url: "/comment",
        data: JSON.stringify({
            "parentId": tid,
            "content": content,
            "type": 1
        }),
        success: function (response) {
            console.log(response);
        }
    });
}