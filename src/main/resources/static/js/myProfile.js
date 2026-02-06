const token = localStorage.getItem("token");

// GET own data
fetch("/api/user/me", {
    headers: {
        "Authorization": "Bearer " + token
    }
})
.then(res => res.json())
.then(user => {
    document.getElementById("firstName").value = user.firstName;
    document.getElementById("lastName").value = user.lastName;
    document.getElementById("email").value = user.email;
    document.getElementById("mobile").value = user.mobile;
    document.getElementById("role").value = user.role;
    document.getElementById("drivingLicense").value = user.drivingLicense;
    document.getElementById("countryCode").value = user.countryCode;
    document.getElementById("gender").value = user.gender;
    document.getElementById("dob").value = user.dob;
    document.getElementById("address1").value = user.address1;
    document.getElementById("address2").value = user.address2;
    document.getElementById("postalCode").value = user.postalCode;
    document.getElementById("technology").value = user.technology;
    document.getElementById("experience").value = user.experience;
    document.getElementById("candidateStatus").value = user.candidateStatus;
    document.getElementById("linkedinUrl").value = user.linkedinUrl;
    document.getElementById("githubUrl").value = user.githubUrl;
    document.getElementById("uploadResume").value = user.uploadResume;
    document.getElementById("portfolioUrl").value = user.portfolioUrl;
    
});


function updateProfile() {
    fetch("/api/user/me", {
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
        alert("Profile updated");
    })
    .catch(err => alert(err.message));
}
