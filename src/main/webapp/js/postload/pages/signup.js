document.querySelector("#create-account").onclick = (e) => {
  e.preventDefault()
  let username = document.querySelector("#input-username").value
  let password = document.querySelector("#input-password").value
  let confirmPass = document.querySelector("#input-password-confirm").value
  signup(username, password, confirmPass)
}

document.querySelector("#cancel").onclick = (e) => {
  e.preventDefault()
  window.location.href = "./login.html"
}