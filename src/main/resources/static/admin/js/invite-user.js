function generateLink() {
	fetch("/api/user/invite", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			"Authorization": "Bearer " + localStorage.getItem("token")
		},
		body: JSON.stringify({
			email: document.getElementById("email").value
		})
	})
		.then(res => res.text())
		.then(link => {
			document.getElementById("inviteLink").value = link;
		});
}

const token = new URLSearchParams(window.location.search).get("token");

function register() {
	fetch("/api/user/register", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({
			token: token,
			password: password.value
		})
	})
		.then(res => res.text())
		.then(msg => {
			alert(msg);
			window.location.href = "/login.html";
		});
}

function registerInviteUser() {
	const password = document.getElementById("password").value;
	const confirmPassword = document.getElementById("confirmPassword").value;

	if (password !== confirmPassword) {
		alert("Password & Confirm Password not match");
		return;
	}

	const params = new URLSearchParams(window.location.search);
	const token = params.get("token");

	fetch("/api/user/invite/register", {
		method: "POST",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify({
			token: token,
			password: password
		})
	})
		.then(res => {
			if (!res.ok) throw new Error("Registration failed");
			return res.json();
		})
		.then(data => {
			alert("Registration successful");
			window.location.href = "/login.html";
		})
		.catch(err => alert(err.message));
}




let currentPage = 0;
const pageSize = 10;

function loadInvites(page = 0) {

    currentPage = page;

    const email = document.getElementById("searchEmail").value;
    const status = document.getElementById("statusFilter").value;
    const token = localStorage.getItem("token");

    let url = `/api/user/invite?page=${page}&size=${pageSize}`;

    if (email) url += `&email=${email}`;
    if (status) url += `&status=${status}`;

    fetch(url, {
        headers: {
            "Authorization": "Bearer " + token
        }
    })
    .then(res => {
        if (!res.ok) throw new Error("Failed to load data");
        return res.json();
    })
    .then(data => {
        renderTable(data.content);   // 👈 yaha bhej rahe
        renderPagination(data);
    })
    .catch(err => alert(err.message));
}

function renderTable(invites) {

	const table = document.getElementById("inviteTable");
	table.innerHTML = "";

	if (invites.length === 0) {
		table.innerHTML = `<tr><td colspan="5">No data found</td></tr>`;
		return;
	}

	invites.forEach((inv, index) => {

		let status = inv.used
			? "USED"
			: new Date(inv.expiryTime) < new Date()
				? "EXPIRED"
				: "PENDING";

		table.innerHTML += `
            <tr>
                <td>${index + 1}</td>
                <td>${inv.email}</td>
                <td>${inv.role.replace("ROLE_", "")}</td>
                <td>
                    <span class="badge ${status.toLowerCase()}">
                        ${status}
                    </span>
                </td>
                <td>
                   
                    <button onclick="deleteInvite('${inv.id}')" 
                        class="btn-action delete">Delete</button>
                </td>
            </tr>
        `;
	});
}


function deleteInvite(id) {

    if (!confirm("Are you sure to delete this invite?")) return;

    fetch(`/api/user/${id}`, {
        method: "DELETE"
    })
    .then(res => res.text())
    .then(msg => {
        alert(msg);
        loadInvites();
    })
    .catch(err => alert("Error deleting invite"));
}


function regenerateInvite(email) {

    fetch(`/api/user/regenerate?email=${email}`, {
        method: "POST"
    })
    .then(res => res.text())
    .then(newLink => {
        alert("New invite generated");
        document.getElementById("inviteLink").value = newLink;
        loadInvites();
    })
    .catch(err => alert("Error regenerating invite"));
}

function renderPagination(data) {
	const pageInfo = document.getElementById("pageInfo");

	pageInfo.innerHTML = `
        Page ${data.number + 1} of ${data.totalPages}
        <br>
        <button ${data.first ? "disabled" : ""} onclick="loadInvites(${data.number - 1})">
            Prev
        </button>

        <button ${data.last ? "disabled" : ""} onclick="loadInvites(${data.number + 1})">
            Next
        </button>
    `;
}

function logout() {
    localStorage.clear();
    window.location.href = "/login.html";
}
// initial load
loadInvites();
