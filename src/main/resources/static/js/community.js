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
        success: function (responseDTO) {
            if (responseDTO.status) {
                $("#comment-content").val('');
            } else {
                if (responseDTO.noLoggedIn != null && responseDTO.noLoggedIn != "") {
                    let isAccepted = confirm(responseDTO.noLoggedIn);
                    if (isAccepted) {
                        window.open("/signin");
                        window.localStorage.setItem("closable", true);
                    }
                }
            }
        }
    });
}