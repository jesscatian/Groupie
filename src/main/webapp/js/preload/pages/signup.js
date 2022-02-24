const signup = (username, password, confirmPassword) => {
  document.querySelector(".error-msg").classList.add("hidden")
  if (username.length == 0 || password.length == 0 || password !== confirmPassword) {
    if (username.length == 0) {
      document.querySelector(".error-msg > p").innerText = "Username cannot be empty"
    }
    else {
      if (password.length == 0) {
        document.querySelector(".error-msg > p").innerText = "Password cannot be empty"
      }
      else {
        if (password !== confirmPassword) {
          document.querySelector(".error-msg > p").innerText = "Passwords do not match"
        }
      }
    }
    document.querySelector(".error-msg").classList.remove("hidden")
    return;
  }
  ajaxPost(ENDPOINT_URL + "/auth/signUp", {"username": username, "psw": password}, (response) => {
    let json = JSON.parse(response)
    if (!json.status) {
      document.querySelector(".error-msg > p").innerText = json.message
      document.querySelector(".error-msg").classList.remove("hidden")
    }
    else {
      window.location.href = "./login.html"
    }
  })
};
