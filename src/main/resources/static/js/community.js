/**
 * 提交回复
 */
function post() {
    let tid = $("#tid").val();
    let content = $("#comment-content").val();

    if (!content) {
        alert("评论不能为空");
        return;
    }

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
        success: function (resultDTO) {
            if (resultDTO.status) {
                window.location.reload();
            } else {
                if (resultDTO.errorMsg != null && resultDTO.errorMsg === "noLoggedIn") {
                    let isAccepted = confirm("请先登录再进行操作");
                    if (isAccepted) {
                        window.open("/signin");
                        window.localStorage.setItem("closable", true);
                    }
                }
            }
        }
    });
}

/**
 * 展开二级评论
 */
function collapseComment(e) {
    let id = e.getAttribute("data-id");
    $("#comment-" + id).toggleClass("in");
}