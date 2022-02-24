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
const closeMe = (d, b) => {
    d.style.display = "none";
    b.style.display = "none";
}

const getResponses = (container) => {
    let eventName = localStorage.getItem("eventNameForResponse");
    let eventID = localStorage.getItem("eventIDForResponse");
    let usernames = JSON.parse(localStorage.getItem("usernamesForResponse"));

    container.innerHTML = "";
    let title = document.createElement("h3");
    title.innerHTML = eventName + "<br>Responses";
    container.append(title);

    for (let i=0; i<usernames.length; i++) {
        ajaxGet(ENDPOINT_URL + "/response/get?event_id="+eventID+"&username=" + usernames[i], (response) => {
            let json = JSON.parse(response)
            appendResult(json, usernames[i], container)
        })
    }
}

const appendResult = (json, username, container) => {
    let usernameNode = document.createTextNode("Username: "+username);
    let availabilityNode = document.createTextNode("Availability: Unknown");
    let excitementNode = document.createTextNode("Excitement: Unknown");

    if (json.status) {
        availabilityNode.textContent = "Availability: "+json.data.availability;
        excitementNode.textContent = "Excitement: "+json.data.excitement;
    }
    container.appendChild(usernameNode)
    container.appendChild(document.createElement('br'))
    container.appendChild(availabilityNode)
    container.appendChild(document.createElement('br'))
    container.appendChild(excitementNode)
    container.appendChild(document.createElement('br'))
    container.appendChild(document.createElement('br'))
    container.appendChild(document.createElement('br'))
}