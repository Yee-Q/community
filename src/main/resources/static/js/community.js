function comment(e) {

    let cid = e.getAttribute("data-id");
    let content = $("#reply-" + cid).val();
    comment2Target(cid, 2, content);
}

function comment2Target(targetId, type, content) {

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
            "parentId": targetId,
            "content": content,
            "type": type
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
 * 提交回复
 */
function post() {

    let tid = $("#tid").val();
    let content = $("#comment-content").val();
    comment2Target(tid, 1, content);
}

/**
 * 展开收起二级评论
 */
function collapseComment(e) {

    let id = e.getAttribute("data-id");
    let subCommentContainer = $("#comment-" + id);
    if (subCommentContainer.children().length != 2) {
        $("#comment-" + id).toggleClass("in");
    } else {
        $.getJSON("/comment/" + id, function (data) {
            $.each( data.data.reverse(), function(index, comment) {

                let mediaImg = $("<img/>", {
                    "class": "media-object img-rounded",
                    "src": comment.user.avatarUrl
                });

                let mediaLeftLink = $("<a/>", {
                    "href": "#"
                });

                let mediaLeft = $("<div/>", {
                    "class": "media-left media-top",
                });

                let mediaHeadingContent= $("<p/>", {
                    html: comment.content
                });

                let mediaHeadingTime= $("<span/>", {
                    "class": "topic-gmt-time text-desc",
                    html: moment(comment.gmtCreate).format("YYYY-MM-DD")
                });

                let mediaHeadingLink = $("<a/>", {
                    "href": "#",
                    "class": "user-name-link"
                }).append($("<strong/>", {
                    html: comment.user.userName
                }));

                let mediaHeading = $("<p/>", {
                    "class": "media-heading",
                });

                let mediaBody = $("<div/>", {
                    "class": "media-body",
                });

                let media = $("<div/>", {
                    "class": "media",
                });

                mediaLeftLink.append(mediaImg);
                mediaLeft.append(mediaLeftLink);
                media.append(mediaLeft);
                mediaHeading.append(mediaHeadingLink);
                mediaHeading.append(mediaHeadingTime);
                mediaBody.append(mediaHeading);
                mediaBody.append(mediaHeadingContent);
                media.append(mediaBody);
                subCommentContainer.prepend(media);
            });
        });
        $("#comment-" + id).toggleClass("in");
    }
}

/**
 * 选择标签
 */
function selectTag(e) {

    let value = e.getAttribute("data-tag");
    let previous = $("#tag").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + "," + value);
        } else {
            $("#tag").val(value);
        }
    }
}

/**
 * 展示标签选择栏
 */
function showSelectTag() {

    $("#select-tag").show();
}