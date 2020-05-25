import React, { useContext } from "react";
import Home from "./Components/Home/Trip/Index";
import Logout from "./Components/Auth/Logout";
import Login from "./Components/Auth/Login";
import Profile from "./Components/Profile/Profile";
import ProfileTrips from "./Components/Profile/Trips/ProfileTrips";
import ProfileDetails from "./Components/Profile/ProfileDetails";
import ActiveTrip from "./Components/Profile/Trips/ActiveTrips";
import ProfileAchievments from "./Components/Profile/ProfileAchievments";
import CreateTrip from "./Components/Admin/Trip/Create";
import EditTrip from "./Components/Admin/Trip/Edit";
import IndexTrip from "./Components/Admin/Trip/Index";
import IndexParticipants from "./Components/Admin/Trip/Participants/Index";
import Register from "./Components/Register";
import { Redirect, Switch, Route } from "react-router-dom";
import TripDetail from "./Components/Home/Trip/Detail";
import IndexAchievement from "./Components/Admin/Achievement/Index";
import CreateAchievement from "./Components/Admin/Achievement/Create";
import EditAchievement from "./Components/Admin/Achievement/Edit";
import IndexCategory from "./Components/Admin/Category/Index";
import CreateCategory from "./Components/Admin/Category/Create";
import EditCategory from "./Components/Admin/Category/Edit";
import IndexUser from "./Components/Admin/User/Index";
import IndexEnrollment from "./Components/Admin/Enrollment/Index";
import CloseEnrollment from "./Components/Admin/Enrollment/Close";
import { appContext } from "./appContext";
import UserReview from "./Components/Profile/UserReview";
import IndexFilter from "./Components/Home/Trip/IndexFilter";

function Router(props) {
    const context = useContext(appContext);

    const ROLE_SUPERUSER = "SUPERUSER";
    const ROLE_ADMIN = "ADMIN";
    const ROLE_USER = "USER";

    const allowAuth = (component) => {
        if (context.user != null) {
            return component;
        } else {
            return <Redirect to={{ pathname: "/login" }} />;
        }
    };

    const allowAuthWithRole = (component, role) => {
        if (context.user != null) {
            if (
                context.user.role === role ||
                (role === ROLE_ADMIN && context.user.role === ROLE_SUPERUSER)
            ) {
                return component;
            } else {
                return <Redirect to={{ pathname: "/" }} />;
            }
        } else {
            return <Redirect to={{ pathname: "/login" }} />;
        }
    };

    const allowGuest = (component) => {
        if (context.user === null) {
            return component;
        } else {
            return <Redirect to={{ pathname: "/" }} />;
        }
    };

    return (
        <div>
            <Route
                path="/profile"
                render={() => {
                    return allowAuthWithRole(<Profile />, ROLE_USER);
                }}
            />
            <Route path="/profile/details" component={ProfileDetails} />
            <Route path="/profile/achievments" component={ProfileAchievments} />
            <Route path="/profile/trips" component={ProfileTrips} />
            <Route path="/profile/trips/active" component={ActiveTrip} />
            {/*<Route path="/profile/reviews" component={UserReview} />*/}

            <Switch>
                {/*Global*/}
                <Route
                    path="/login"
                    exact={true}
                    render={() => {
                        return allowGuest(<Login />);
                    }}
                />
                <Route
                    path="/logout"
                    render={() => {
                        return allowAuth(<Logout />);
                    }}
                />
                {/*User*/}
                <Route
                    path="/register"
                    exact={true}
                    render={() => {
                        return allowGuest(<Register />);
                    }}
                />
                <Route path="/" exact={true} component={Home} />
                <Route path="/trips" exact={true} component={IndexFilter} />

                <Route path="/trips/:id" component={TripDetail} />
                {/*Admin*/}
                <Route
                    path="/trip"
                    exact
                    render={() => {
                        return allowAuthWithRole(<IndexTrip />, ROLE_ADMIN);
                    }}
                />
                <Route
                    path="/trip/create"
                    exact
                    render={() => {
                        return allowAuthWithRole(<CreateTrip />, ROLE_ADMIN);
                    }}
                />

                <Route
                    path="/trip/:id/edit"
                    exact
                    render={() => {
                        return allowAuthWithRole(<EditTrip />, ROLE_ADMIN);
                    }}
                />

                <Route
                    path="/trip/:id/participants"
                    exact
                    render={() => {
                        return allowAuthWithRole(
                            <IndexParticipants />,
                            ROLE_ADMIN
                        );
                    }}
                />

                <Route
                    path="/achievement"
                    exact={true}
                    render={() => {
                        return allowAuthWithRole(
                            <IndexAchievement />,
                            ROLE_ADMIN
                        );
                    }}
                />

                <Route
                    path="/achievement/create"
                    exact={true}
                    render={() => {
                        return allowAuthWithRole(
                            <CreateAchievement />,
                            ROLE_ADMIN
                        );
                    }}
                />

                <Route
                    path="/achievement/:id/edit"
                    exact={true}
                    render={() => {
                        return allowAuthWithRole(
                            <EditAchievement />,
                            ROLE_ADMIN
                        );
                    }}
                />

                <Route
                    path="/category"
                    exact={true}
                    render={() => {
                        return allowAuthWithRole(<IndexCategory />, ROLE_ADMIN);
                    }}
                />

                <Route
                    path="/category/create"
                    exact={true}
                    render={() => {
                        return allowAuthWithRole(
                            <CreateCategory />,
                            ROLE_ADMIN
                        );
                    }}
                />

                <Route
                    path="/category/:id/edit"
                    exact={true}
                    render={() => {
                        return allowAuthWithRole(<EditCategory />, ROLE_ADMIN);
                    }}
                />

                <Route
                    path="/close"
                    exact={true}
                    render={() => {
                        return allowAuthWithRole(
                            <IndexEnrollment />,
                            ROLE_ADMIN
                        );
                    }}
                />

                <Route
                    path="/close/:id"
                    exact={true}
                    render={() => {
                        return allowAuthWithRole(
                            <CloseEnrollment />,
                            ROLE_ADMIN
                        );
                    }}
                />

                <Route
                    path="/user"
                    exact
                    render={() => {
                        return allowAuthWithRole(<IndexUser />, ROLE_ADMIN);
                    }}
                />
            </Switch>
        </div>
    );
}

function mapStateToProps(state) {
    const { auth } = state;

    return {
        auth,
    };
}
/*
export default connect(
    mapStateToProps
)(Router);
*/
export default Router;
