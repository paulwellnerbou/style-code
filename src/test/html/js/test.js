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
        }
    );
};

var updateIframeContents = function(textarea, iframe) {
    iframe.contentWindow.document.body.innerHTML = textarea.value;
};

var convertToEditable = function(codeElement) {
    var textarea = document.createElement('textarea');
    textarea.setAttribute('class', 'form-control');
    textarea.value = unescapeHtml(codeElement.innerHTML);
    var parentNode = codeElement.parentNode;
    parentNode.appendChild(textarea);
    parentNode.removeChild(codeElement);
    return textarea;
};

var createRenderIframe = function(textareaElement) {
    var newPre = document.createElement('pre');
    var iframe = document.createElement('iframe');
    iframe.setAttribute('src', 'iframe.html');
    iframe.setAttribute('class', 'form-control');
    iframe.setAttribute('style', 'min-height: 100px; resize: both; overflow: auto;');

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
