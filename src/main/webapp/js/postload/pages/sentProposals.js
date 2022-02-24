window.addEventListener('load', (event) => {
   event.preventDefault()
   const container = document.getElementById("results")
   getProposals(container);
});

window.onload = (event) => {
  console.log('page is fully loaded');
};