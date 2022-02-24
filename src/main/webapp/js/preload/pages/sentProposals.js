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
const closeMe = (d, b) => {
    d.style.display = "none";
    b.style.display = "none";
}

const finalizeProp = (eventId, proposalId) =>{
    ajaxPost(ENDPOINT_URL + "/proposal/finalized?finalized_event_id=" + eventId + "&proposal_id=" + proposalId,
    {EVENT_ID: eventId, PROPOSAL_ID: proposalId}, (response) => {alert(JSON.parse(response).status)})
}
const getProposals = (resultsContainer) => {
    let senderUsername = localStorage.getItem("username");
      ajaxGet(ENDPOINT_URL + "/proposal/get?type=sent&username=" + senderUsername, (response) => {
          let json = JSON.parse(response)
          showResults(json, resultsContainer)
      })
}

const getRecommendedEventID = (proposalID) => {
    ajaxGet(ENDPOINT_URL + "/EventRecommender?proposalID=" + proposalID, (response) => {
        let json = JSON.parse(response)
        console.log(json)
    })
}

const showResults = (json = {}, container) => {
  container.innerHTML = ""
  if (JSON.stringify(json) !== "{}") {
    if (!json.status) {
      let msg = document.createElement("p")
      msg.className = "error"
      msg.innerHTML = "No results"
      container.appendChild(msg)
    }
    else {
      let msg = document.createElement("p")
      msg.innerHTML = (json.data.length.toString() + " result(s)").trim()
      container.appendChild(msg)
      console.log(json)
      for (const proposal of json.data) {
          let proposalDiv = document.createElement("div")
          proposalDiv.style.cssText = "margin-bottom:100px";
          proposalDiv.className = "result"
          let p = document.createElement("p")
          p.className = "subtitle"
          for (let i = 0; i < proposal.events.length; i++){
              let divE = document.createElement('div')
              let divText = document.createTextNode("Proposal Title: " + proposal.proposalTitle)
              let receive = document.createTextNode("Receiver: " + proposal.receiverUsernames)
              let ev = document.createElement("a")
              ev.href = proposal.events[i].url
              ev.title = proposal.events[i].url
              ev.appendChild(document.createTextNode("Event: " + proposal.events[i].name))
              let ti = document.createTextNode("Time: " + proposal.events[i].time)
              let da = document.createTextNode("Date: " + proposal.events[i].date)
              let a = document.createElement('a')
              a.onclick = function() {
                  localStorage.setItem("eventNameForResponse",proposal.events[i].name);
                  localStorage.setItem("eventIDForResponse",proposal.events[i].eventID);
                  localStorage.setItem("usernamesForResponse",JSON.stringify(proposal.receiverUsernames));
              }
              a.href = ENDPOINT_URL+"/eventResponse.html";
              a.appendChild(document.createTextNode("View responses"))
            divE.appendChild(divText)
            divE.appendChild(document.createElement('br'))
            divE.appendChild(receive)
            divE.appendChild(document.createElement('br'))
            divE.appendChild(ev)
            divE.appendChild(document.createElement('br'))
            divE.appendChild(ti)
            divE.appendChild(document.createElement('br'))
            divE.appendChild(da)
            divE.appendChild(document.createElement('br'))
            divE.appendChild(a)
            proposalDiv.appendChild(divE)
            let b = document.createElement("button")
            b.id = "prop-button";
            b.textContent = i==0 ? "Recommended: Select as Final Event" : "Select as Final Event";
            b.onclick = function(){
                finalizeProp(proposal.events[i].eventID, proposal.proposalID);
                alert("Event "+proposal.events[i].name+" is selected as the final event for proposal "+proposal.proposalTitle+".");
            };
            proposalDiv.appendChild(b)
        }
        container.appendChild(proposalDiv)
      }
    }
  }
}