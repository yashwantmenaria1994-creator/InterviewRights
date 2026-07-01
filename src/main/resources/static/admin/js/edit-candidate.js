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
		document.getElementById("firstName").value = data.firstName || "";
		document.getElementById("lastName").value = data.lastName || "";
		document.getElementById("email").value = data.email || "";
		document.getElementById("mobile").value = data.mobile || "";
		document.getElementById("role").value = data.role || "";
		document.getElementById("drivingLicense").value = data.drivingLicense || "";
		document.getElementById("countryCode").value = data.countryCode || "";
		document.getElementById("gender").value = data.gender || "";
		document.getElementById("dob").value = data.dob || "";
		document.getElementById("address1").value = data.address1 || "";
		document.getElementById("address2").value = data.address2 || "";
		document.getElementById("postalCode").value = data.postalCode || "";
		document.getElementById("technology").value = data.technology || "";
		document.getElementById("experience").value = data.experience || "";
		document.getElementById("candidateStatus").value = data.candidateStatus || "";
		document.getElementById("linkedinUrl").value = data.linkedinUrl || "";
		document.getElementById("githubUrl").value = data.githubUrl || "";
		document.getElementById("portfolioUrl").value = data.portfolioUrl || "";

		document.getElementById("workAuthorization").value = data.workAuthorization || "";
		document.getElementById("sponsorshipRequired").value = data.sponsorshipRequired || "";
		document.getElementById("visaType").value = data.visaType || "";
		document.getElementById("visaExpiry").value = data.visaExpiry || "";
		document.getElementById("passportNumber").value = data.passportNumber || "";
		document.getElementById("citizenshipCountry").value = data.citizenshipCountry || "";

		// Show visa section if needed
		if (data.workAuthorization === "VISA_HOLDER") {
			document.getElementById("visaSection").style.display = "block";
		}

		// Show profile picture
		if (data.profilePic) {
			const preview = document.getElementById("profilePreview");
			preview.src = data.profilePic;
			preview.style.display = "block";

			preview.onerror = function() {
				console.error("Image failed to load:", data.profilePic);
				this.style.display = "none";
			};
		}
	})
	.catch(err => {
		console.error("Error loading candidate:", err);
	});

// Update candidate
// Update candidate
function updateCandidate() {
	const formData = new FormData();

	// Normal fields
	formData.append("firstName", document.getElementById("firstName").value);
	formData.append("lastName", document.getElementById("lastName").value);
	formData.append("mobile", document.getElementById("mobile").value);
	formData.append("role", document.getElementById("role").value);
	formData.append("drivingLicense", document.getElementById("drivingLicense").value);
	formData.append("countryCode", document.getElementById("countryCode").value);
	formData.append("gender", document.getElementById("gender").value);
	formData.append("dob", document.getElementById("dob").value);
	formData.append("address1", document.getElementById("address1").value);
	formData.append("address2", document.getElementById("address2").value);
	formData.append("postalCode", document.getElementById("postalCode").value);
	formData.append("technology", document.getElementById("technology").value);
	formData.append("experience", document.getElementById("experience").value);
	formData.append("candidateStatus", document.getElementById("candidateStatus").value);
	formData.append("linkedinUrl", document.getElementById("linkedinUrl").value);
	formData.append("githubUrl", document.getElementById("githubUrl").value);
	formData.append("portfolioUrl", document.getElementById("portfolioUrl").value);

	formData.append("workAuthorization", document.getElementById("workAuthorization").value);
	formData.append("sponsorshipRequired", document.getElementById("sponsorshipRequired").value);
	formData.append("visaType", document.getElementById("visaType").value);
	formData.append("visaExpiry", document.getElementById("visaExpiry").value);
	formData.append("passportNumber", document.getElementById("passportNumber").value);
	formData.append("citizenshipCountry", document.getElementById("citizenshipCountry").value);

	// Files
	const profilePic = document.getElementById("profilePic").files[0];
	if (profilePic) {
		formData.append("profilePic", profilePic);
	}

	const resume = document.getElementById("uploadResume").files[0];
	if (resume) {
		formData.append("uploadResume", resume);
	}

	fetch(`/api/admin/candidates/${id}`, {
		method: "PUT",
		headers: {
			"Authorization": "Bearer " + token
			// Content-Type set mat karo
		},
		body: formData
	})
		.then(res => {
			if (!res.ok) throw new Error("Update failed");
			return res.json();
		})
		.then(() => {
			alert("Candidate updated successfully");
			window.location.href = "/admin/candidate.html";
		})
		.catch(err => {
			console.error(err);
			alert(err.message);
		});
}

document.getElementById("workAuthorization").addEventListener("change", function() {

	const visaSection = document.getElementById("visaSection");

	if (this.value === "VISA_HOLDER") {
		visaSection.style.display = "block";
	} else {
		visaSection.style.display = "none";
	}

});
function previewProfilePic() {
	const input = document.getElementById("profilePic");
	const preview = document.getElementById("profilePreview");

	if (input.files && input.files[0]) {
		const reader = new FileReader();

		reader.onload = function(e) {
			preview.src = e.target.result;
			preview.style.display = "block";
		};

		reader.readAsDataURL(input.files[0]);
	}
}

