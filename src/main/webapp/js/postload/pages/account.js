const logout = () => {
  localStorage.removeItem("uuid")
  window.location.href = "./index.html"
}
const dashboard = () => {
    window.location.href = "./dashboard.html"
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
function back(){
    window.location.href = "./dashboard.html"
}
const displayAvailability = () => {
    const container = document.getElementById("unav")
    var user = localStorage.getItem("username");
    ajaxGet(ENDPOINT_URL + "/Availability?type=getUnavailabilities&username=" + user, (response) => {
      let json = JSON.parse(response)
        let divE = document.createElement('div')
        let st = document.createTextNode("Start: " + json.data[(json.data.length)-1].start)
        let e = document.createTextNode("End: " + json.data[(json.data.length)-1].end)
        divE.appendChild(st)
        divE.appendChild(document.createElement('br'))
        divE.appendChild(e)
        container.appendChild(divE)
      }
    )
}
const setAvailability = (startDate, endDate) => {
var user = localStorage.getItem("username");
console.log(startDate.value.length)
if (startDate.value.length && endDate.value.length) {
  ajaxGet(ENDPOINT_URL + "/Availability?type=addUnavailability&username=" + user + "&start=" + startDate.value + "T00:00:00&end=" + endDate.value +"T00:00:00", (response) => {
  let json = JSON.parse(response)
  alert("Unavailability has been set!")
     })
    }
  else{
  alert("Error: Please select start and end date.")
  }
}
document.querySelector("#submit-avail").onclick = (e) => {
    e.preventDefault()
    const startDate = document.getElementById("start-date")
    const endDate = document.getElementById("end-date")
    setAvailability(startDate, endDate);
}

document.querySelector("#search-button").onclick = (e) => {
 e.preventDefault()
 let username = document.querySelector("#username").value
 let c = $(".search-users")
 search(username,c);
}

const search = (username, resultsContainer) => {
 ajaxGet(ENDPOINT_URL + "/search/user?q=" + encodeURIComponent(username.trim()), (response) => {
       let json = JSON.parse(response)
       genResults(json, resultsContainer)
     })

};

const genResults = (json = {}, container) => {
 container.empty();
 if (JSON.stringify(json) !== "{}") {
   if (!json.status) {
     let msg = document.createElement("p")
     msg.className = "error"
     msg.innerHTML = "No results found"
     container.appendChild(msg)
   }
   else {
     for (let i=0;i<json.data.length;i++) {
       let str = `<li class="list-group-item">
                                      <div class="row">
                                      <div class="col-8">
                                          ${json.data[i]}
                                      </div>
                                      <button class="col-3 blocked btn-sm">
                                          Block
                                      </button>
                                      </div>
                                  </li>`;
     container.append(str);
     }
   }
 }
 blockUser();
}

window.onload = function loadList() {
let su = $(".search-users");
 search("",su);
 }

function blockUser() {
let blockList = document.querySelectorAll(".blocked");
    for (let i = 0; i < blockList.length; i++) {

        blockList[i].onclick = function() {
           let userName = $(this).prev().text();
           this.parentNode.parentNode.remove(document.querySelector(".list-group-item"));
           let str = `<li class="list-group-item">
                                                     <div class="row">
                                                     <div class="col-8 unblock">
                                                         ${userName}
                                                     </div>
                                                     <button class="col-3 unblock btn-sm">
                                                         Unblock
                                                     </button>
                                                     </div>
                                                 </li>`;
            $(".blocked-list").append(str);
        unblockUser();
        }
    }
}
function unblockUser() {
let unblockList = document.querySelectorAll(".unblock");
    for (let i = 0; i < unblockList.length; i++) {
        unblockList[i].onclick = function() {
           let userName = $(this).prev().text();
           this.parentNode.parentNode.remove(document.querySelector(".list-group-item"));
           let str = `<li class="list-group-item">
                                      <div class="row">
                                      <div class="col-8">
                                          ${userName}
                                      </div>
                                      <button class="col-3 blocked btn-sm">
                                          Block
                                      </button>
                                      </div>
                                  </li>`;
            $(".search-users").append(str);
        blockUser();
        }
    }
}
blockUser();
unblockUser();
