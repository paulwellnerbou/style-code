var processCodeBlocks = function () {
    [].forEach.call(
        document.querySelectorAll("code.html"),
        function (element) {
            var textarea = convertToEditable(element);
            var iframe = createRenderIframe(textarea);
            iframe.onload = function() {
                updateIframeContents(textarea, iframe);
                textarea.onkeyup = function() {
                    updateIframeContents(textarea, iframe);
                };
            };
            var label = addToggleForCodeView(textarea.parentNode);
            label.onclick = function(ev){toggleCode(label, textarea);};
        }
    );
};

var updateIframeContents = function(textarea, iframe) {
    iframe.contentWindow.document.body.innerHTML = textarea.value;
    var height = $(iframe.contentWindow.document.getElementsByTagName('html')[0]).height();
    height += 2; // border
    $(iframe).css("height", height + "px");
};

function resizeTextarea(textarea) {
    var codeHeight = textarea.getAttribute('data-original-height');
    $(textarea).css('height', codeHeight + "px");
}

var convertToEditable = function(codeElement) {
    var textarea = document.createElement('textarea');
    var codeHeight = $(codeElement).height() + 25;
    textarea.setAttribute('data-original-height', codeHeight);
    resizeTextarea(textarea);
    $(textarea).css('min-height', "50px");
    textarea.setAttribute('class', 'form-control');
    textarea.setAttribute("style", "display:none;");
    textarea.value = unescapeHtml(codeElement.innerHTML);
    var parentNode = codeElement.parentNode;
    parentNode.appendChild(textarea);
    parentNode.removeChild(codeElement);
    return textarea;
};

var addToggleForCodeView = function(preElement) {
    var label = document.createElement('div');
    label.setAttribute("class", "label label-default pointer");
    label.innerHTML = "Code&nbsp;";
    var icon = document.createElement('span');
    icon.setAttribute("class", "glyphicon glyphicon-menu-right");
    label.appendChild(icon);
    var info = document.createElement('small');
    info.setAttribute("class", "right light info");
    info.setAttribute("style", "display:none;");
    info.innerHTML = "Live editing supported, while you type, the rendering result below will update automatically."
    $(preElement).prepend(label);
    $(preElement).prepend(info);
    return label;
};

var toggleCode = function(element, textarea) {
    $(element.nextElementSibling).toggle(200, resizeTextarea(textarea));
    $(element.parentNode.querySelector('.info')).toggle(200);
    var icon = element.querySelector('span.glyphicon');
    $(icon).toggleClass("glyphicon-menu-right");
    $(icon).toggleClass("glyphicon-menu-down");
};

var createRenderIframe = function(textareaElement) {
    var newPre = document.createElement('pre');
    newPre.setAttribute('class', 'iframe');
    var iframe = document.createElement('iframe');
    iframe.setAttribute('class', 'demo');
    iframe.setAttribute('src', 'iframe.html');
    iframe.setAttribute('style', 'resize: both; overflow: auto;');
    var pre = textareaElement.parentNode;
    var parent = pre.parentNode;
    newPre.appendChild(iframe);
    parent.insertBefore(newPre, pre.nextElementSibling);
    return iframe;
};

var unescapeHtml = function (escapedHtml) {
    var e = document.createElement('div');
    e.innerHTML = escapedHtml;
    return e.childNodes.length === 0 ? "" : e.childNodes[0].nodeValue;
};
