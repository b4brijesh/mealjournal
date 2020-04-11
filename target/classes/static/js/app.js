window.app = new Vue({
    el: "#app",
    data: {
        userName: "",
        editProfileFlag: false,
        profileEditData: {
            "fullname": "",
            "dob": null,
            "height": null,
            "weight": null,
            "goal": null
        },

        addMealFormVisible: false,
        newMealName: "",
        newMealDate: null,
        newMealCalories: 0,

        editMealId: null,
        editMealName: "",
        editMealDate: null,
        editMealCalories: 0,

        periodStart: null,
        periodStop: null,
        meals: [],
        errorText: ""
    },
    methods: {
        fetchProfileData: function() {
            console.log("fetching profile data");
            fetch('/profile')
            .then(response => response.json())
            .then(profileEditData => {
                this.profileEditData = profileEditData;
                this.userName = profileEditData.fullname;
            });
        },
        toggleViewProfile: function(){
            this.editProfileFlag = !this.editProfileFlag;
        },
        updateProfileData: function() {
            fetch('/update-profile?' + new URLSearchParams({
                fullname: this.profileEditData.fullname,
                dob: this.profileEditData.dob,
                height: this.profileEditData.height,
                weight: this.profileEditData.weight,
                goal: this.profileEditData.goal
            })).then(this.setError).then(this.fetchProfileData).then(this.toggleViewProfile)
            .then(this.fetchMeals);
        },
        fetchMeals: function() {
            console.log("fetching meals");
            console.log("period start:" + this.periodStart);
            console.log("period stop:" + this.periodStop);
            if(this.periodStart == null || this.periodStop == null) {
                fetch('/getMeals')
                .then(response => response.json())
                .then(meals => this.setColor(meals))
                .then(coloredMeals => this.sortMeals(coloredMeals))
                .then(meals => {
                    this.meals = meals;
                });
            } else {
                fetch('/getMeals?' + new URLSearchParams({
                    startDate: this.periodStart,
                    stopDate: this.periodStop}))
                .then(response => response.json())
                then(meals => this.setColor(meals))
                .then(coloredMeals => this.sortMeals(coloredMeals))
                .then(meals => {
                    this.meals = meals;
                });
            }
        },
        fetchMeal: function(mealId) {
            console.log("fetching a meal: " + mealId)
            return fetch('/getMeal?' + new URLSearchParams({
                mealId: mealId}))
            .then(response => response.json())
            .then(this.fetchMeals).then(this.toggleAddMealForm);
        },
        toggleAddMealForm: function() {
            this.addMealFormVisible = !this.addMealFormVisible;
        },
        checkMealForm: function(e) {
            if (this.newMealName && this.newMealDate && this.newMealCalories) {
                return true;
            }
            e.preventDefault();
        },
        addMeal: function() {
            fetch('/addMeal?' + new URLSearchParams({
                newMealName: this.newMealName,
                newMealDate: this.newMealDate,
                newMealCalories: this.newMealCalories}))
            .then(response => response.json())
            .then(this.setError).then(this.fetchMeals).then(this.toggleAddMealForm);
        },
        toggleEditMeal: function(mealId) {
            console.log(mealId);
            if(mealId !== null && mealId !== undefined && mealId !== "") {
                this.editMealId = mealId;
                this.fetchMeal(mealId)
                .then(meal => {
                    console.log("filling edit meal data");
                    this.editMealName = meal.mealName;
                    this.editMealDate = this.formatDate(meal.mealDate);
                    this.editMealCalories = meal.mealCalories;
                });
            } else {
                this.editMealId = null;
                this.editMealName = "";
                this.editMealDate = null;
                this.editMealCalories = 0;
            }
        },
        editMeal: function() {
            fetch('/editMeal?' + new URLSearchParams({
                mealId: this.editMealId,
                mealName: this.editMealName,
                mealDate: this.editMealDate,
                mealCalories: this.editMealCalories}))
            .then(response => response.json())
            .then(this.setError).then(this.fetchMeals).then(this.toggleEditMeal);
        },
        removeMeal: function(removeMealId) {
            console.log("removing meal " + removeMealId);
            fetch('/removeMeal?' + new URLSearchParams({
                mealId: removeMealId}))
            .then(response => response.json())
            .then(this.setError).then(this.fetchMeals);
        },
        formatDate: function(str) {
            let date = new Date(parseInt(str, 10));
            let dd = String(date.getDate()).padStart(2, '0');
            let mm = String(date.getMonth() + 1).padStart(2, '0'); //January is 0!
            let yyyy = date.getFullYear();
            let formattedDate = yyyy + '-' + mm + '-' + dd;
            return formattedDate;
        },
        setColor: function(meals) {
            let goal = this.profileEditData.goal;
            if(goal === null || goal === undefined || goal === "") {
                console.log("goal not set.. returning without coloring");
                return meals;
            }
            goal = parseInt(this.profileEditData.goal);

            console.log("coloring meals");
            let days = new Map();
            for(let i = 0; i < meals.length; i++) {
                meal = meals[i];
                let day = meal.mealDate;
                if(days.has(day)) {
                    let dayMeals = days.get(day);
                    dayMeals.push(meal);
                    days.set(day, dayMeals);
                } else {
                    let dayMeals = [];
                    dayMeals.push(meal);
                    days.set(day, dayMeals);
                }
            }

            let coloredMeals = [];
            for ([day, dayMeals] of days) {
                let consumed = 0;
                for(let i = 0; i < dayMeals.length; i++) {
                    meal = dayMeals[i];
                    consumed = consumed + meal["mealCalories"];
                }
                if(consumed > goal) {
                    for(let i = 0; i < dayMeals.length; i++) {
                        meal = dayMeals[i];
                        meal["color"] = "firebrick";
                        coloredMeals.push(meal);
                    }
                } else {
                    for(let i = 0; i < dayMeals.length; i++) {
                        meal= dayMeals[i];
                        meal["color"] = "darkgreen";
                        coloredMeals.push(meal);
                    }
                }
            }
            return coloredMeals;
        },
        sortMeals: function(coloredMeals) {
            function customSort(a, b) {
                return b.mealDate - a.mealDate;
            }
            return coloredMeals.sort(customSort);
        },
        setError: function(response) {
            if(response.status==='success') return;
            this.errorText = "Error: " + response.errorText;
            console.log(this.errorText);
            let self = this;
            setTimeout(() => {self.errorText = '';}, 2000);
        }
    },
    mounted: function() {
        console.log("mounting");
        this.fetchProfileData();
        setTimeout(this.fetchMeals(), 3000);
        setTimeout(this.fetchMeals(), 3000);
    }
});