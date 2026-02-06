const token = localStorage.getItem("token");


function openEditCandidate(id) {
    console.log("Editing candidate ID:", id);
    window.location.href = `/admin/edit-candidate.html?id=${id}`;
}


fetch("/api/admin/candidates", {
    headers: {
        "Authorization": "Bearer " + token
    }
})
.then(res => res.json())
.then(data => {
    const table = document.getElementById("candidateTable");
    table.innerHTML = "";

    data.forEach((c, index) => {
        table.innerHTML += `
            <tr>
                <td>${index + 1}</td>
                <td>${c.firstName} ${c.lastName}</td>
                <td>${c.email}</td>
                <td>${c.role.replace("ROLE_", "")}</td>
                <td>
                    <span class="${c.active ? 'status-active' : 'status-inactive'}">
                        ${c.active ? 'Active' : 'Inactive'}
                    </span>
                </td>
                <td>
                    <button class="btn-edit" onclick="openEditCandidate('${c.id}')">Edit</button>
                    <button class="btn-delete" onclick="deleteCandidate('${c.id}')">Delete</button>
                </td>
            </tr>
        `;
    });
});
