const API_URL = "http://localhost:8080/api/auth";

function register() {
    fetch(`${API_URL}/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: document.getElementById("email").value,
            password: document.getElementById("password").value,
            firstName: document.getElementById("firstName").value,
            lastName: document.getElementById("lastName").value,
            mobile: document.getElementById("mobile").value
        })
    })
     .then(() => {
        alert("Registration successful! Please login.");
        window.location.href = "/login.html";   // ✅ REDIRECT
    })
    .catch(error => {
        alert(error.message);
    });
}

function login() {
    fetch(`${API_URL}/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        })
    })
    .then(res => res.json())
    .then(data => {
        localStorage.setItem("token", data.token);
        localStorage.setItem("email", data.email);
        localStorage.setItem("firstName", data.firstName);
        localStorage.setItem("lastName", data.lastName);
        localStorage.setItem("mobile", data.mobile); // ✅ store email
        window.location.href = "/dashboard.html"; // ✅ redirect
    })
    .catch(() => alert("Invalid credentials"));
}
