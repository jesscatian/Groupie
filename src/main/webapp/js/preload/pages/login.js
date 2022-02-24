let failedAttemptNum = 0;
let bannedUsername = "";
let bannedDate = new Date();

const login = (username, password) => {
  document.querySelector(".error-msg").classList.add("hidden")
  if (username.length == 0 || password.length == 0) {
    if (username.length == 0) {
      document.querySelector(".error-msg > p").innerText = "Username cannot be empty"
    }
    else {
      if (password.length == 0) {
        document.querySelector(".error-msg > p").innerText = "Password cannot be empty"
      }
    }
    document.querySelector(".error-msg").classList.remove("hidden")
    return;
  }

  if (failedAttemptNum >= 3 && username == bannedUsername && ((new Date()).getTime()-bannedDate.getTime())/1000 < 60) {
    document.querySelector(".error-msg > p").innerText = "More than 3 attempts have faild. Please try again after " + (60-((new Date()).getTime()-bannedDate.getTime())/1000).toPrecision(2) + " seconds."
    document.querySelector(".error-msg").classList.remove("hidden")
    return;
  } else if (username != bannedUsername) {
    failedAttemptNum = 0;
  }

  ajaxGet(ENDPOINT_URL + "/auth/login?username=" + username + "&psw=" + password, (response) => {
    let json = JSON.parse(response)
    if (!json.status) {
      failedAttemptNum++;
      bannedUsername = username;
      bannedDate = new Date();
      document.querySelector(".error-msg > p").innerText = json.message
      document.querySelector(".error-msg").classList.remove("hidden")
    }
    else {
      localStorage.setItem("uuid", json.data.uuid)
      localStorage.setItem("username", json.data.username)
      window.location.href = "./dashboard.html"
    }
  })
};