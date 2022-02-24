let ENDPOINT_URL = "http://localhost:8080"
function ajaxPost(endpointUrl, postData, returnFunction){
    var xhr = new XMLHttpRequest();
    xhr.open('POST', endpointUrl, true);
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.onreadystatechange = function(){
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status == 200) {
                returnFunction( xhr.responseText );
            } else {
                alert('AJAX Error.');
                console.log(xhr.status);
            }
        }
    }
    xhr.send(JSON.stringify(postData));
};
function ajaxGet(endpointUrl, returnFunction){
    var xhr = new XMLHttpRequest();
    xhr.open('GET', endpointUrl, true);
    xhr.onreadystatechange = function(){
        if (xhr.readyState == XMLHttpRequest.DONE) {
            if (xhr.status == 200) {
                // When ajax call is complete, call this function, pass a string with the response
                returnFunction( xhr.responseText );
            } else {
                alert('AJAX Error.');
                console.log(xhr.status);
            }
        }
    }
    xhr.send();
};

$(window).on("load", () => {
  $("*[onenter]").on("keyup", (e) => {
    if (e.which === 13) {
      e.preventDefault()
      eval(e.target.getAttribute("onenter"))
    }
  })
})

function idleTimer() {
    let t;
    window.onmousemove = resetTimer;
    window.onmousedown = resetTimer;
    window.onclick = resetTimer;
    window.onscroll = resetTimer;
    window.onkeypress = resetTimer;

    function logout() {
        console.log(localStorage.getItem("username"));
        if (localStorage.getItem("username") == null) {return;}
        localStorage.removeItem("username")
        localStorage.removeItem("uuid")
        window.location.href = "./index.html"
        alert("You have been safely logged out due to being inactive for more than 60 seconds.")
    }

    function resetTimer() {
        clearTimeout(t);
        t = setTimeout(logout, 60000);
    }
}

idleTimer();
