// Redirect if not logged in
const token = localStorage.getItem("token");
const email = localStorage.getItem("email");
const firstName = localStorage.getItem("firstName");
const lastName = localStorage.getItem("lastName");
const mobile = localStorage.getItem("mobile");

if (!token) {
    window.location.href = "/login.html";
}

// Set email in header
document.getElementById("userEmail").innerText = firstName + " " + lastName;


function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("email");
    
    window.location.href = "/login.html";
}


