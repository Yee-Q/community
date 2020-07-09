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

/**
 * 登录
 */
function signin() {

    let username = $("#inputSigninUserName").val();
    let password = $("#inputSigninPassword").val();

    $.ajax({
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        url: "/signin",
        data: JSON.stringify({
            "userName": username,
            "password": password,
            "flag": true
        }),
        success: function (resultDTO) {
            if (resultDTO.status) {
                window.location.reload();
            } else {
                let errorMsg = resultDTO.errorMsg;
                if (errorMsg != null) {
                    if (errorMsg === "userNameIsNull") {
                        $("#usernameSigninErrorMsg").text("用户名不能为空");
                    } else if (errorMsg === "userNameIsOutOfRange") {
                        $("#usernameSigninErrorMsg").text("用户名长度为 3 - 7 位");
                    } else if (errorMsg === "passwordIsNull") {
                        $("#passwordSigninErrorMsg").text("密码不能为空");
                    } else if (errorMsg === "passwordIsOutOfRange") {
                        $("#passwordSigninErrorMsg").text("密码长度为 3 - 7 位");
                    } else if (errorMsg === "userIsNotExist") {
                        $("#usernameSigninErrorMsg").text("用户名不存在");
                    } else if (errorMsg === "passwordError") {
                        $("#passwordSigninErrorMsg").text("密码错误");
                    }
                }
            }
        }
    });
}

/**
 * 注册
 */
function register() {

    let formData = new FormData();

    let username = $("#inputRegistUserName").val();
    let password = $("#inputRegistPassword").val();
    let file = $('#loadHeadPortrait').get(0).files[0]

    if (file == null) {
        $("#fileRegisterErrorMsg").text("请选择上传图片");
    }

    formData.append('file', file);
    formData.append('userDTO', JSON.stringify({
        "userName": username,
        "password": password,
        "flag": false
    }))

    $.ajax({
        type: "POST",
        dataType: "formData",
        processData: false,
        contentType: false,
        url: "/register",
        data: formData,
        success: function (resultDTO) {
            if (resultDTO.status) {
                window.location.reload();
            } else {
                let errorMsg = resultDTO.errorMsg;
                if (errorMsg != null) {
                    if (errorMsg === "userNameIsNull") {
                        $("#usernameRegisterErrorMsg").text("用户名不能为空");
                    } else if (errorMsg === "userNameIsOutOfRange") {
                        $("#usernameRegisterErrorMsg").text("用户名长度为 3 - 7 位");
                    } else if (errorMsg === "passwordIsNull") {
                        $("#passwordRegisterErrorMsg").text("密码不能为空");
                    } else if (errorMsg === "passwordIsOutOfRange") {
                        $("#passwordRegisterErrorMsg").text("密码长度为 3 - 7 位");
                    } else if (errorMsg === "UserIsAlreadyExist") {
                        $("#usernameSigninErrorMsg").text("用户名已经存在");
                    } else if (errorMsg === "ProfileImgNotLoad") {
                        $("#fileRegisterErrorMsg").text("请选择上传图片");
                    }
                }
            }
        }
    });
}