import React, { useEffect } from "react";
import logo from "./logo.svg";
import "./App.css";
//na routovanie v aplikacii
import Router from "./Router";
//kniznica na HTTP dotazy
import Navigation from "./Components/Navigation";
import { library } from "@fortawesome/fontawesome-svg-core";
import { BrowserRouter } from "react-router-dom";
import {
    faTrophy,
    faPowerOff,
    faCog,
    faSuitcase,
    faStar,
    faStarHalf,
    faSwimmer,
    faMedal,
    faAward,
    faCommentMedical,
    faCommentDots,
    faShieldAlt,
    faCrown,
    faWater,
    faSwimmingPool,
    faLocationArrow,
    faTrashAlt,
    faGraduationCap,
    faFish,
    faMountain,
    faFlag,
    faSnowflake,
    faHamburger,
    faPizzaSlice,
    faGlassCheers,
    faPastafarianism,
    faCompass,
    faRunning,
    faCampground,
    faChessRook,
    faChevronLeft,
    faTimes,
    faClock,
    faMapMarkerAlt,
    faMapSigns,
    faCalendarAlt,
    faUserAlt,
    faMoneyBill,
    faMinusCircle,
    faCheckCircle,
    faMoneyBillAlt,
    faAddressCard,
    faCheck,
    faRedo,
} from "@fortawesome/free-solid-svg-icons";
import { faStar as emptyStar } from "@fortawesome/free-regular-svg-icons";
import { appContext } from "./appContext";
import Cookies from "js-cookie";
import { Container, Spinner } from "react-bootstrap";

//allow use string names of icons from FontAwesome
library.add(
    faTrophy,
    faPowerOff,
    faCog,
    faSuitcase,
    faStar,
    faStarHalf,
    emptyStar,
    faSwimmer,
    faMedal,
    faAward,
    faCommentMedical,
    faCommentDots,
    faShieldAlt,
    faCrown,
    faWater,
    faSwimmingPool,
    faLocationArrow,
    faTrashAlt,
    faGraduationCap,
    faFish,
    faMountain,
    faFlag,
    faSnowflake,
    faHamburger,
    faPizzaSlice,
    faGlassCheers,
    faPastafarianism,
    faCompass,
    faRunning,
    faCampground,
    faChessRook,
    faChevronLeft,
    faTimes,
    faClock,
    faMapMarkerAlt,
    faMapSigns,
    faCalendarAlt,
    faUserAlt,
    faMoneyBill,
    faMinusCircle,
    faCheckCircle,
    faMoneyBillAlt,
    faAddressCard,
    faCheck,
    faRedo
);

class App extends React.Component {
    state = {
        user: null,
        loginTry: false,
    };

    logout = () => {
        Cookies.remove("JSESSIONID");
        this.setState({ user: null });
    };

    login = (user) => {
        this.setState({ user: user });
        console.log("from login");
        console.log(this.state.user);
    };

    componentDidMount = async () => {
        if (Cookies.get("JSESSIONID") && this.state.user == null) {
            console.log("tryiiiiiiing");
            await this.tryLogin();
            console.log("after try");
        }
        console.log("after condition");
        this.setState({ tryLogin: true });
    };

    tryLogin = async () => {
        console.log("try login");

        await fetch(BASE_API_URL + "/user/current", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
        })
            .then((response) => {
                if (response.ok) return response.json();
                else throw Error(response.status);
            })
            .then((data) => {
                this.login(data);
                console.log("after login");
            })
            .catch((error) => {
                this.logout();
                console.log("after logout");
                console.error(error);
            });
    };

    render() {
        const value = {
            user: this.state.user,
            logout: this.logout,
            login: this.login,
            tryLogin: this.tryLogin,
        };

        console.log(value);
        if (this.state.tryLogin) {
            return (
                <appContext.Provider value={value}>
                    <BrowserRouter>
                        <div className="App">
                            <Navigation />
                            <Router />
                            {/* router z Router.js */}
                        </div>
                    </BrowserRouter>
                </appContext.Provider>
            );
        } else {
            return (
                <Container className="p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        }
    }
}

export default App;
export const BASE_API_URL = "http://localhost:8080";
