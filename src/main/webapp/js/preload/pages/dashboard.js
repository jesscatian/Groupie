var userList = [];
var eventList = [];
var events = [];
var myName;
class Event {
  constructor(name, date, time, url, genre) {
    this.name = name;
    this.date = date;
    this.time = time;
    this.url = url;
    this.genre = genre;
  }
}

const logout = () => {
  localStorage.removeItem("uuid")
  window.location.href = "./index.html"
}
const account = () => {
  window.location.href = "./account.html"
}
const pendingInvites = () => {
  window.location.href = "./pendinginvites.html"
}
const proposalResponse = () => {
  window.location.href = "./proposalResponse.html"
}
const sentProposals = () => {
window.location.href = "./sentProposals.html"
}
const calendar = () => {
window.location.href = "./calendar.html"
}
const search = (keywords, genre, country, startDate, endDate, errMsg, code, resultsContainer) => {
  if (keywords.value.length < 1) {
    errMsg.innerHTML = "Keywords cannot be empty"
    errMsg.classList.remove("hidden")
    genResults(resultsContainer)
    return;
  }
  else {
    errMsg.innerHTML = ""
    errMsg.classList.add("hidden")
  }
  let add = ""
  if (COUNTRIES[code]) {
    add = ("&countryCode=" + code)
  }
  $("#search-button").addClass("loading")

  if (startDate.value.length && endDate.value.length) {
    ajaxGet(ENDPOINT_URL + "/search/event?keyword=" + encodeURIComponent(keywords.value.trim()) + "&startDate=" + startDate.value + "&endDate=" + endDate.value + add, (response) => {
      let json = JSON.parse(response)
      genResults(json, resultsContainer)
      $("#search-button").removeClass("loading")
    })
  }
  else {
    ajaxGet(ENDPOINT_URL + "/search/event?keyword=" + encodeURIComponent(keywords.value.trim()) + add, (response) => {
      let json = JSON.parse(response)
      if (genre.value.length){
        ajaxGet(ENDPOINT_URL + "/search/event?genre=" + encodeURIComponent(genre.value.trim()) + "&genre" + genre.value + add, (response) => {
           let json = JSON.parse(response)
           genResults(json, resultsContainer)
           $("#search-button").removeClass("loading")
           })
     }
      else{
        genResults(json, resultsContainer)
      }
      $("#search-button").removeClass("loading")
    })
  }
}

const genResults = (json = {}, container) => {
  container.innerHTML = ""
  var count = 1
  if (JSON.stringify(json) !== "{}") {
    if (!json.status) {
      let msg = document.createElement("p")
      msg.className = "error"
      msg.innerHTML = "No results"
      container.appendChild(msg)
    }
    else {
      let msg = document.createElement("p")
      msg.innerHTML = (json.data.events.length.toString() + " result(s)").trim()
      container.appendChild(msg)
      for (let event of json.data.events) {
        let result = document.createElement("div")
        result.className = "result"
        let resultContent = document.createElement("div")
        resultContent.className = "result-content"
        let p = document.createElement("p")
        p.className = "subtitle"
        p.innerHTML = moment(event.date).format("MMMM Do YYYY")
        let a = document.createElement("a")
        a.href = event.url
        a.innerHTML = event.name
        let b = document.createElement("b")
        b.innerHTML = '<br>' + event.time
        var box = document.createElement("input");
        box.id = "eventsBox" + count;
        count++
        box.type = "checkbox"
        box.value = event.url
        resultContent.appendChild(p)
        resultContent.appendChild(a)
        resultContent.appendChild(b)
        result.appendChild(box);
        result.appendChild(resultContent)
        container.appendChild(result)
        const newEvent = new Event(event.name, event.date, event.time, event.url, event.genre)
        events.push(newEvent)
        console.log(newEvent)
        box.setAttribute("onclick", "handleEvents(this)")
      }
    }
  }
}

function handleEvents(e){
  let res = e.nextSibling.children[1].href.toString()
  console.log(res)
  for (let i=0; i<events.length; i++){
    if(res === events[i].url.toString()){
      eventList.push(events[i])
    }
  }
}


const country = (element) => {
  element.value = element.value.trim().toUpperCase()
  const code = element.value
  if (COUNTRIES[code]) {
    element.style.backgroundImage = ("url('https://flagcdn.com/h60/" + code.toLowerCase() + ".png')")
  }
  else {
    element.style.backgroundImage = "url('../../assets/images/dummy-flag.png')"
  }
}

function next(){
  let array = [];
  let checkedEntries = document.querySelectorAll('input[type=checkbox]:checked')

  for (let i = 0; i < checkedEntries.length; i++) {
    array.push(checkedEntries[i].value)
  }
  console.log(array);

  if(eventList.length < 1){
    alert("Please add at least one event")
  } else {
    document.querySelector(".main").style.display = "none";
    document.querySelector(".main2").style.display = "flex";
    for (let i=0; i<eventList.length; i++){
      console.log(eventList[i])
    }
  }

}

function clear(){
  document.querySelector(".main2").style.display = "none";
}

function searchUsers(){
  let username = document.getElementById("username").value
  var currentUser = localStorage.getItem("username")
      let url = "http://localhost:8080/search/user?q=" + username
      ajaxGet(url, (response) => {
        let json = JSON.parse(response)
        for(let i=0; i<json.data.length; i++) {
          if (username == json.data[i]) {
            var names = document.createElement("span")
            names.innerHTML = json.data[i]
            document.querySelector("#results2").appendChild(names)
            var box = document.createElement("input")
            box.id = "usersBox"
            box.type = "checkbox"
            box.setAttribute("onclick", "handleClick(this)")
            document.querySelector("#results2").appendChild(box)
          }
        }
      })
}

function handleClick(cb) {
  if(cb.checked == true){
    userList.push(cb.previousSibling.innerHTML)
  } else {
    userList.splice(userList.indexOf(cb.previousSibling.innerHTML), 1)
  }
}

function setName(name){
  proposalName = name.innerHTML;
}

function submit(){
  if(userList.length < 1){
    alert("Please add at least one user")
  } else if(document.getElementById("proposalName").value === "") {
    alert("Please add a proposal name")
  }
  else {
    myName = document.getElementById("proposalName").value
    ajaxPost(ENDPOINT_URL + "/proposal/send", {
      "proposalTitle":myName,
      senderUsername: localStorage.getItem("username"),
      receiverUsernames: userList,
      events: eventList
    }, (response) => {
      console.log(JSON.parse(response).status)
    })
    alert("Proposal sent!")
    window.location.href = "./dashboard.html"
  }



}


