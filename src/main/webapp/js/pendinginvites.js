const logout = () => {
  localStorage.removeItem("uuid");
  window.location.href = "./index.html";
}
const account = () => {
  window.location.href = "./account.html";
}
const pendingInvites = () => {
  window.location.href = "./pendinginvites.html";
}
const proposalResponse = () => {
  window.location.href = "./proposalResponse.html";
}
const sentProposals = () => {
window.location.href = "./sentProposals.html";
}
const calendar = () => {
window.location.href = "./calendar.html";
}
var finalizedProposals = [];
class FinalizedProposal {
    constructor(name, date, time, url, genre, proposalID) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.url = url;
        this.genre = genre;
        this.proposalID = proposalID;
    }
}


function back(){
    window.location.href = "./dashboard.html";
}

function check(id){
    alert("Accepted Invite")
    var currentUser = localStorage.getItem("username");
    let url = "http://localhost:8080/response/finalized/send?final_response=1&username=" + currentUser;
    url += "&proposal_id=" + id;
    ajaxPost(url, {final_response: 1, username: currentUser, proposal_id: id}, (response) => {
        let json = JSON.parse(response);
        console.log(json);
    })
}

function cross(id){
    alert("Declined Invite");
    var currentUser = localStorage.getItem("username");
    let url = "http://localhost:8080/response/finalized/send?final_response=0&username=" + currentUser;
    url += "&proposal_id=" + id;
    ajaxPost(url, {final_response: 0, username: currentUser, proposal_id: id}, (response) => {
        let json = JSON.parse(response);
        console.log(json);
    })
}

function populate(){
    var done = false;
    var currentUser = localStorage.getItem("username");
    let url = "http://localhost:8080/proposal/get?type=received&username=" + currentUser;
    ajaxGet(url, (response) => {
        let json = JSON.parse(response);
        for(let i=0; i<json.data.length; i++){
            console.log(json.data[i]);
            if(json.data[i].finalizedEventID != null){
                for(let j=0; j<json.data[i].events.length; j++){
                    if(json.data[i].events[j].eventID === json.data[i].finalizedEventID){
                        var name = json.data[i].events[j].name;
                        var date = json.data[i].events[j].date;
                        var time = json.data[i].events[j].time;
                        var url = json.data[i].events[j].url;
                        var genre = json.data[i].events[j].genre;
                        var proposalID = json.data[i].proposalID;
                        const newFinalizedProposal = new FinalizedProposal(name, date, time, url, genre, proposalID);
                        finalizedProposals.push(newFinalizedProposal);
                    }
                }

            }
            if (i === json.data.length-1){
                finalize();
            }
        }
    })
}

function finalize(){
    let str = "";
    for(let i=0; i<finalizedProposals.length; i++){

        var currentUser = localStorage.getItem("username");
        var response;
        var flag;

        ajaxGet("http://localhost:8080/response/finalized/get?username=" + currentUser + "&proposal_id=" + finalizedProposals[i].proposalID, (response) => {
            let json = JSON.parse(response);
            response = json.data;
            if(response === null){
                flag = "Not responded";
            } else if(response === true){
                flag = "Accepted";
            } else {
                flag = "Declined";
            }
            str +=
                '        <div class="card">\n' +
                '            <img src="assets/images/invited.png" alt="" class="card_image">\n' +
                '            <div class="card_content">\n' + `<p>${flag}</p>` +
                `<a href="${finalizedProposals[i].url}">${finalizedProposals[i].proposalID}</a>` + '<br>' +  `Date: ${finalizedProposals[i].date}` +  '<br>' + `Time: ${finalizedProposals[i].time}` + '<br>' + `Genre: ${finalizedProposals[i].genre}` +
                '            </div>\n' +
                '            <div class="card_info">\n' +
                '                <span class="material-icons" name="check" onclick="check(\''+finalizedProposals[i].proposalID+'\')">done</span>\n' +
                '                <span class="material-icons" name="cross" onclick="cross(\''+finalizedProposals[i].proposalID+'\')">close</span>\n' +
                '            </div>\n' +
                '        </div>\n';
            document.getElementById("cards").innerHTML = str;
        })
    }
}