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
            logout(); // auto logout if unauthorized
        }
        return res;
    });
}

// ================= LOAD CANDIDATES =================
document.addEventListener("DOMContentLoaded", function () {

    const tbody = document.getElementById("candidateTable");
    if (!tbody) return; // safety for other pages

    authFetch("/api/admin/candidates")
        .then(res => {
            if (!res.ok) {
                throw new Error("HTTP Error: " + res.status);
            }
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
                        </td>
                    </tr>
                `);
            });
        })
        .catch(err => {
            console.error("Candidate load failed:", err);
            alert("Unable to load candidates");
        });
});

// ================= ACTIONS =================
function editCandidate(id) {
    window.location.href = `/admin/edit-candidate.html?id=${id}`;
}

function deleteCandidate(id) {
    if (!confirm("Are you sure you want to delete this candidate?")) return;

    authFetch(`/api/admin/candidates/${id}`, {
        method: "DELETE"
    }).then(res => {
        if (res.ok) {
            alert("Candidate deleted successfully");
            location.reload();
        } else {
            alert("Delete failed");
        }
    });
}
