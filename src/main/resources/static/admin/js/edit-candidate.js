const token = localStorage.getItem("token");
const params = new URLSearchParams(window.location.search);
const id = params.get("id");

if (!id) {
    alert("Invalid candidate");
    window.location.href = "/admin/candidate.html";
}
// Load candidate data
fetch(`/api/admin/candidates/${id}`, {
    headers: {
        "Authorization": "Bearer " + token
    }
})
.then(res => res.json())
.then(data => {
    document.getElementById("firstName").value = data.firstName;
    document.getElementById("lastName").value = data.lastName;
    document.getElementById("email").value = data.email;
    document.getElementById("mobile").value = data.mobile;
    document.getElementById("role").value = data.role;
    document.getElementById("drivingLicense").value = data.drivingLicense;
    document.getElementById("countryCode").value = data.countryCode;
    document.getElementById("gender").value = data.gender;
    document.getElementById("dob").value = data.dob;
    document.getElementById("address1").value = data.address1;
    document.getElementById("address2").value = data.address2;
    document.getElementById("postalCode").value = data.postalCode;
    document.getElementById("technology").value = data.technology;
    document.getElementById("experience").value = data.experience;
    document.getElementById("candidateStatus").value = data.candidateStatus;
    document.getElementById("linkedinUrl").value = data.linkedinUrl;
    document.getElementById("githubUrl").value = data.githubUrl;
    document.getElementById("uploadResume").value = data.uploadResume;
    document.getElementById("portfolioUrl").value = data.portfolioUrl;
  });

// Update candidate
function updateCandidate() {
    fetch(`/api/admin/candidates/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({
             firstName: document.getElementById("firstName").value,
            lastName: document.getElementById("lastName").value,
            mobile: document.getElementById("mobile").value,
            role: document.getElementById("role").value,
            drivingLicense: document.getElementById("drivingLicense").value,
            countryCode: document.getElementById("countryCode").value,
            gender: document.getElementById("gender").value,
            dob: document.getElementById("dob").value,
            address1: document.getElementById("address1").value,
            address2: document.getElementById("address2").value,
            postalCode: document.getElementById("postalCode").value,
            technology: document.getElementById("technology").value,
            experience: document.getElementById("experience").value,
            candidateStatus: document.getElementById("candidateStatus").value,
            linkedinUrl: document.getElementById("linkedinUrl").value,
            githubUrl: document.getElementById("githubUrl").value,
            portfolioUrl: document.getElementById("portfolioUrl").value,
            uploadResume: document.getElementById("uploadResume").value
        })
    })
    .then(res => {
        if (!res.ok) throw new Error("Update failed");
        alert("Candidate updated successfully");
        window.location.href = "/admin/candidate.html";
    })
    .catch(err => alert(err.message));
}
