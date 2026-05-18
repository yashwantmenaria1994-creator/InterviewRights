// ================= LOGOUT =================
function logout() {
    localStorage.clear();
    window.location.href = "/login.html";
}

// ================= AUTH CHECK =================
const token = localStorage.getItem("token");
if (!token) {
    window.location.href = "/login.html";
}

// ================= COMMON FETCH =================
function authFetch(url, options = {}) {
    return fetch(url, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token,
            ...(options.headers || {})
        }
    }).then(res => {
        if (res.status === 401 || res.status === 403) {
            logout();
        }
        return res;
    });
}

// ================= ON LOAD =================
document.addEventListener("DOMContentLoaded", function () {
    loadCandidates();
    loadDashboardCounts();
});

// ================= LOAD CANDIDATES =================
function loadCandidates() {

    const tbody = document.getElementById("candidateTable");
    if (!tbody) return;

    authFetch("/api/admin/candidates")
        .then(res => {
            if (!res.ok) throw new Error(res.status);
            return res.json();
        })
        .then(data => {
            tbody.innerHTML = "";

            data.content.forEach((user, index) => {

                const statusClass = user.active ? "status-active" : "status-inactive";
                const statusText = user.active ? "Active" : "Inactive";
                const role = user.role.replace("ROLE_", "");

                tbody.insertAdjacentHTML("beforeend", `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                        <td>${role}</td>
                        <td class="${statusClass}">${statusText}</td>
                        <td>
                            <button class="btn btn-edit" onclick="editCandidate('${user.id}')">Edit</button>
                            ${role !== 'ADMIN' 
                                ? `<button class="btn btn-delete" onclick="deleteCandidate('${user.id}')">Delete</button>` 
                                : ''
                            }
                            <button class="btn btn-edit" onclick="openScheduleModal('${user.email}')">
                                Schedule
                            </button>
                        </td>
                    </tr>
                `);
            });
        })
        .catch(err => {
            console.error(err);
            alert("Unable to load candidates");
        });
}

// ================= ACTIONS =================
function editCandidate(id) {
    window.location.href = `/admin/edit-candidate.html?id=${id}`;
}

function deleteCandidate(id) {
    if (!confirm("Are you sure?")) return;

    authFetch(`/api/admin/candidates/${id}`, {
        method: "DELETE"
    }).then(res => {
        if (res.ok) {
            alert("Deleted ✅");
            loadCandidates();
        } else {
            alert("Delete failed ❌");
        }
    });
}

// ================= SCHEDULE MODAL =================
function openScheduleModal(email) {
    document.getElementById("scheduleModal").style.display = "block";
    document.getElementById("candidateEmail").value = email;
}

function closeScheduleModal() {
    document.getElementById("scheduleModal").style.display = "none";
}

// ✅ FIXED FUNCTION
function scheduleInterview() {

    const data = {
        email: document.getElementById("candidateEmail").value,
        date: document.getElementById("interviewDate").value,
        time: document.getElementById("interviewTime").value,
        link: document.getElementById("interviewLink").value,
        expiry: document.getElementById("expiryTime").value,
        interviewerEmail: document.getElementById("interviewerEmail").value

    };

    if (!data.date || !data.time || !data.link) {
        alert("Fill all fields");
        return;
    }

    authFetch("/api/interview/schedule", {
        method: "POST",
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) throw new Error();
        return res.text();
    })
    .then(() => {
        alert("Interview Scheduled ✅");
        closeScheduleModal();
        loadDashboardCounts(); // ✅ correct place
    })
    .catch(() => {
        alert("Error ❌");
    });
}

// ================= CLICK OUTSIDE MODAL =================
window.onclick = function(event) {
    const modal = document.getElementById("scheduleModal");
    if (event.target === modal) {
        closeScheduleModal();
    }
};

// ================= DASHBOARD COUNT =================
function loadDashboardCounts() {

    authFetch("/api/interview/count")
        .then(res => {
            if (!res.ok) throw new Error();
            return res.json();
        })
        .then(data => {
            document.getElementById("scheduledCount").innerText = data.scheduled || 0;
            document.getElementById("completedCount").innerText = data.completed || 0;
        })
        .catch(err => {
            console.error(err);
        });
}