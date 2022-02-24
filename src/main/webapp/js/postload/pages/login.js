document.querySelector(".on-enter-target").onclick = (e) => {
  e.preventDefault()
  let username = document.querySelector("#input-username").value
  let password = document.querySelector("#input-password").value
  login(username, password)
}
