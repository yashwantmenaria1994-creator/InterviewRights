const BASE_URL = "http://interviewright-332017328.us-east-1.elb.amazonaws.com:8080";
const API_URL = `${BASE_URL}/api/auth`;

function resetPassword() {
    const token = document.getElementById("token").value;
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    if (!newPassword || !confirmPassword) {
        alert("Please fill all fields");
        return;
    }

    if (newPassword !== confirmPassword) {
        alert("Passwords do not match");
        return;
    }

    fetch(`${API_URL}/reset-password`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            token: token,
            newPassword: newPassword
        })
    })
    .then(res => {
        if (!res.ok) throw new Error("Failed to reset password");
        return res.text();
    })
    .then(msg => {
        alert(msg);
        window.location.href = "/login.html";
    })
    .catch(err => alert(err.message));
}
