let currentTheme = "light";

document.addEventListener("DOMContentLoaded", () => {
  changeTheme();
});

function changeTheme() {
  // set to webpage
  document.querySelector("html").classList.add(currentTheme);

  //set the event listener to changeTheme button
  const changeThemeButton = document.querySelector("#theme_change_button");

  changeThemeButton.addEventListener("click", () => {
    document.querySelector("html").classList.remove(currentTheme);
    changeThemeButton.querySelector("span").textContent =
      currentTheme == "light" ? "Light" : "Dark";

    console.log(currentTheme);

    if (currentTheme === "dark") {
      currentTheme = "light";
    } else {
      currentTheme = "dark";
    }

    setTheme(currentTheme);

    document.querySelector("html").classList.add(currentTheme);
  });
}

// set theme to local storage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

// get theme from local storage
function getTheme() {
  let theme = localStorage.getItem("theme");
  return theme ? theme : "light";
}
