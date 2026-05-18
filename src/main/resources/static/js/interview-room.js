let faceViolations = 0;
let multipleFaceViolations = 0;
let candidateEmail = "";
let faceInterval;
let cooldown = false;

const params =
	new URLSearchParams(
		window.location.search
	);

const role =
	params.get("role")
	|| "candidate";

const token =
	params.get("token");

const room =
	params.get("room");


let localStream;
let peerClient;
let currentCall;

let monitoringStarted = false;
let interviewId = null;

let timerInterval;
let seconds = 0;
let riskScore = 100;
let tabViolations = 0;
let interviewPassed = true
let faceSimilarity = 0;

window.onload = async function() {

	document.getElementById(
		"roleBadge"
	).innerText =
		role.toUpperCase();

	const startBtn =
		document.getElementById("startBtn");
	if (role === "candidate" && startBtn) {
		startBtn.style.display = "none";
	}

	if (role === "interviewer") {

		let monitor =
			document.getElementById(
				"monitorSection"
			);

		if (monitor) {
			monitor.style.display =
				"none";
		}

		let title =
			document.getElementById(
				"monitorTitle"
			);

		if (title) {
			title.innerText =
				"Interviewer Controls";
		}

	}


	if (!room) {
		alert("Room id missing");
		return;
	}


	await startCamera();


	if (role === "candidate") {
		await validateToken();          // token validate
		await verifyCandidateFace();    // face verify
	} else {
		showAlert("Click Start Interview to begin.");
	}

};



async function validateToken() {
	try {
		if (!token) return;

		const response = await fetch(
			"/api/interview/validate?token=" + token
		);

		if (!response.ok) {
			throw new Error("Token validation failed");
		}

		const data = await response.json();

		interviewId = data.interviewId;
		candidateEmail = data.email;

		console.log("Interview ID:", interviewId);
		console.log("Candidate Email:", candidateEmail);

		if (!candidateEmail) {
			throw new Error("Candidate email not found");
		}

	} catch (error) {
		console.error("validateToken error:", error);
		alert("Invalid interview token.");
		window.location.href =
			"/interview-terminated.html?reason=INVALID_TOKEN";
	}
}

async function verifyCandidateFace() {
	try {
		showAlert("Verifying face...");

		// Webcam snapshot capture
		const canvas = document.createElement("canvas");
		canvas.width = localVideo.videoWidth;
		canvas.height = localVideo.videoHeight;

		const ctx = canvas.getContext("2d");
		ctx.drawImage(localVideo, 0, 0);

		const blob = await new Promise(resolve =>
			canvas.toBlob(resolve, "image/jpeg")
		);
		console.log("Candidate Email:", candidateEmail);

		// FormData
		const formData = new FormData();
		formData.append("file", blob, "live.jpg");
		formData.append("candidateEmail", candidateEmail);

		// API call
		const response = await fetch("/api/face/verify", {
			method: "POST",
			body: formData
		});

		const result = await response.json();
		faceSimilarity = Number(result.similarity || 0);

		if (result.matched) {
			showAlert(
				"Face Verified (" +
				faceSimilarity.toFixed(2) +
				"% Match)"
			);

			alert(
				"Face Verified Successfully\n\n" +
				"Similarity: " +
				faceSimilarity.toFixed(2) +
				"%"
			);
			await initCall();
			showAlert("Waiting for interviewer to start...");
		} else {
			alert(
				"Face does not match profile photo.\n\n" +
				"Similarity: " +
				faceSimilarity.toFixed(2) +
				"%"
			);

			window.location.href =
				"/interview-terminated.html?reason=FACE_MISMATCH";
		}

	} catch (error) {
		console.error("Face verification failed:", error);
		alert("Face verification failed.");
		window.location.href =
			"/interview-terminated.html?reason=FACE_VERIFICATION_ERROR";
	}
}


async function startCamera() {

	try {

		localStream =
			await navigator.mediaDevices
				.getUserMedia({
					video: true,
					audio: true
				});

		localVideo.srcObject =
			localStream;

		await localVideo.play();

		console.log(
			"camera started"
		);

	} catch (e) {

		console.error(e);

		alert(
			"Allow camera/mic"
		);

	}

}




async function startInterview() {

	if (monitoringStarted)
		return;

	monitoringStarted = true;

	await initCall();

	if (role === "candidate") {

		startTimer();

		await loadModels();

		trackTabSwitch();

	}

}




function startTimer() {

	timerInterval =
		setInterval(() => {

			seconds++;

			let m =
				Math.floor(seconds / 60);

			let s =
				seconds % 60;

			document.getElementById(
				"interviewTimer"
			).innerText =
				String(m).padStart(2, "0")
				+ ":"
				+ String(s).padStart(2, "0");

		}, 1000);

}





async function initCall() {

	peerClient =
		new Peer(
			role + "-" + room,
			{
				host: "0.peerjs.com",
				port: 443,
				secure: true
			}
		);



	peerClient.on(
		"open",
		id => {

			console.log(
				"My Peer Id:",
				id
			);


			if (role === "candidate") {

				setTimeout(() => {

					console.log(
						"Calling interviewer-" + room
					);

					let call =
						peerClient.call(
							"interviewer-" + room,
							localStream
						);


					if (!call) {

						showAlert(
							"Interviewer not online"
						);

						return;
					}


					currentCall = call;


					call.on("stream", async remoteStream => {

						console.log("Remote stream received");

						remoteVideo.srcObject = remoteStream;
						await remoteVideo.play().catch(console.error);

						if (role === "candidate" && !monitoringStarted) {
							monitoringStarted = true;

							startTimer();
							await loadModels();
							trackTabSwitch();
						}
					});


					call.on(
						"error",
						e => console.error(e)
					);


				}, 5000);

			}

		});




	peerClient.on(
		"call",
		call => {

			console.log(
				"Incoming call"
			);

			currentCall = call;

			call.answer(
				localStream
			);


			call.on(
				"stream",
				remoteStream => {

					console.log(
						"Interviewer received remote"
					);

					remoteVideo.srcObject =
						remoteStream;

					remoteVideo.play()
						.catch(console.error);

				});


			call.on(
				"error",
				e => console.error(e)
			);


		});


	peerClient.on(
		"error",
		err => {
			console.error(
				"Peer Error:",
				err
			);
		}
	);


	showAlert(
		"Video Connecting..."
	);

}




function showAlert(msg) {

	let a =
		document.getElementById(
			"alerts"
		);

	if (a) {
		a.innerText = msg;
	}

}




async function loadModels() {

	try {

		document.getElementById(
			"faceStatus"
		).innerText =
			"Loading...";

		await faceapi.nets
			.tinyFaceDetector
			.loadFromUri(
				"/models"
			);

		document.getElementById(
			"faceStatus"
		).innerText =
			"Monitoring";

		startFaceMonitoring();

	} catch (e) {

		console.error(e);

	}

}

function startFaceMonitoring() {

	faceInterval = setInterval(
		async () => {

			try {

				let faces =
					await faceapi.detectAllFaces(
						localVideo,
						new faceapi.TinyFaceDetectorOptions({
							inputSize: 320,
							scoreThreshold: 0.3
						})
					);


				if (faces.length === 0) {

					faceViolations++;

					updateRisk(10);

					document.getElementById(
						"faceStatus"
					).innerText =
						"No Face";

					showAlert(
						"Face Not Visible"
					);

					checkTermination();

				}
				else if (faces.length > 1) {

					multipleFaceViolations++;

					updateRisk(20);

					document.getElementById(
						"faceStatus"
					).innerText =
						"Multiple Faces";

					showAlert(
						"Multiple Faces Detected"
					);

					checkTermination();

				}
				else {

					document.getElementById("faceStatus").innerText =
						"Candidate Visible";

				}

			} catch (e) {
				console.error(e);
			}

		}, 3000);

}

function updateRisk(points) {

	riskScore -= points;

	if (riskScore < 0) {
		riskScore = 0;
	}

	document.getElementById(
		"risk"
	).innerText =
		riskScore;

}

function checkTermination() {

	if (riskScore <= 40) {

		terminateInterview(
			"LOW_RISK_SCORE"
		);

		return;
	}

	if (tabViolations >= 3) {

		terminateInterview(
			"TAB_LIMIT_EXCEEDED"
		);

		return;
	}

	if (faceViolations >= 3) {

		terminateInterview(
			"FACE_NOT_VISIBLE"
		);

		return;
	}

}
async function terminateInterview(
	reason = "AUTO_TERMINATED"
) {

	clearInterval(
		timerInterval
	);

	clearInterval(
		faceInterval
	);


	let finalReport = {

		interviewId,
		room,

		riskScore,

		tabViolations,

		faceViolations,

		multipleFaceViolations,

		faceSimilarity,

		interviewDuration:
			seconds,

		passed: false,

		terminated: true,

		terminateReason:
			reason

	};


	console.log(
		"TERMINATED REPORT",
		finalReport
	);


	/* reliable save */
	navigator.sendBeacon(
		"/api/trust/finish",
		new Blob(
			[JSON.stringify(finalReport)],
			{
				type: "application/json"
			}
		)
	);


	cleanupResources();

	window.location.href =
		"/interview-terminated.html";

}


function cleanupResources() {

	if (currentCall) {
		currentCall.close();
	}

	if (peerClient) {
		peerClient.destroy();
	}

	if (localStream) {

		localStream
			.getTracks()
			.forEach(
				t => t.stop()
			);

	}

	clearInterval(
		timerInterval
	);

	clearInterval(
		faceInterval
	);

}

function trackTabSwitch() {

	function registerViolation(
		msg,
		penalty
	) {

		if (cooldown) return;

		cooldown = true;

		setTimeout(() => {
			cooldown = false;
		}, 500);

		tabViolations++;

		updateRisk(penalty);

		document.getElementById(
			"tabCount"
		).innerText =
			tabViolations;

		showAlert(msg);

		checkTermination();

	}


	document.addEventListener(
		"visibilitychange",
		() => {

			if (document.hidden) {

				registerViolation(
					"Tab Switch Detected",
					15
				);

			}

		});


	window.addEventListener(
		"blur",
		() => {

			registerViolation(
				"Window Focus Lost",
				10
			);

		});

}



async function shareScreen() {

	try {

		await navigator.mediaDevices
			.getDisplayMedia({
				video: true
			});

		showAlert(
			"Screen Shared"
		);

	} catch (e) {
		console.error(e);
	}

}




window.onbeforeunload = () => {

	cleanupResources();

};

function finishInterview() {

	clearInterval(
		timerInterval
	);

	let finalReport = {

		interviewId: interviewId,

		candidateEmail: candidateEmail,

		room: room,

		riskScore: riskScore,

		faceSimilarity: faceSimilarity,

		tabViolations:
			tabViolations,

		faceViolations:
			faceViolations,

		multipleFaceViolations:
			multipleFaceViolations,

		interviewDuration:
			seconds,

		passed:
			riskScore >= 60
			&&
			tabViolations < 3
			&&
			faceViolations < 3

	};


	console.log(
		"FINAL REPORT",
		finalReport
	);


	fetch(
		"/api/trust/finish",
		{
			method: "POST",
			headers: {
				"Content-Type":
					"application/json"
			},
			body: JSON.stringify(
				finalReport
			)
		}
	)
		.then(() => {

			window.location.href =
				"/interview-result.html"
				+
				"?score=" + riskScore
				+
				"&facematch=" + faceSimilarity.toFixed(2)
				+
				"&tab=" + tabViolations
				+
				"&face=" + faceViolations
				+
				"&multi=" + multipleFaceViolations
				+
				"&time=" + seconds
				+
				"&pass=" + (riskScore >= 60);

		});

}