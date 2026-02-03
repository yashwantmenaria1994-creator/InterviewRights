const API_URL = "http://localhost:8080/api/auth";

/* ================= REGISTER ================= */
function register() {

    const payload = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        mobile: document.getElementById("mobile").value
    };

    fetch(`${API_URL}/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
    .then(res => {
        if (!res.ok) throw new Error("Registration failed");
        return res.text();
    })
    .then(() => {
        alert("Registration successful! Please login.");
        window.location.href = "/login.html";
    })
    .catch(err => alert(err.message));
}

/* ================= LOGIN ================= */
function login() {

    fetch(`${API_URL}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        })
    })
    .then(res => {
        if (!res.ok) throw new Error("Invalid credentials");
        return res.json();
    })
    .then(data => {
        if (!data.token) throw new Error("Token not received");

        // 🔐 Save token
        localStorage.setItem("token", data.token);
        localStorage.setItem("email", data.email);
        localStorage.setItem("firstName", data.firstName);
        localStorage.setItem("lastName", data.lastName);
        localStorage.setItem("mobile", data.mobile);
        localStorage.setItem("token", data.token);
		localStorage.setItem("role", data.role);


        // ✅ Decode JWT & get role
        const decoded = parseJwt(data.token);
        const role = decoded.role;

        console.log("User Role:", role);

        // 🔁 Redirect based on role
        if (role === "ROLE_ADMIN") {
            window.location.href = "/admin/admin-dashboard.html";
        } else {
            window.location.href = "/dashboard.html";
        }
    })
    .catch(err => alert(err.message));
}

function parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    return JSON.parse(atob(base64));
}

/* ================= FORGOT PASSWORD ================= */
function forgotPassword() {

    const email = document.getElementById("email").value;

    if (!email) {
        alert("Please enter your email");
        return;
    }

    fetch(`${API_URL}/forgot-password`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email })
    })
    .then(res => {
        if (!res.ok) throw new Error("Unable to process request");
        return res.text();
    })
    .then(msg => alert(msg))
    .catch(() => alert("Something went wrong"));
}
