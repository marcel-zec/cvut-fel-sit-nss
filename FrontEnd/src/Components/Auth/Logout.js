import React from "react";
import { Container, Spinner } from "react-bootstrap";
import { Redirect } from "react-router-dom";
import { appContext } from "../../appContext";

class Logout extends React.Component {
    state = {
        logout: false,
    };

    static contextType = appContext;

    componentDidMount() {
        fetch(`http://localhost:8080/logout`, {
            method: "GET",
            mode: "cors",
            credentials: "include",
        }).then((response) => {
            //TODO - osetrenie vynimiek
            if (response.ok) {
                this.setState({ logout: true });
                this.context.logout();
            } else {
                return null;
            }
        });
    }

    render() {
        if (this.state.logout) {
            return <Redirect to="/login" />;
        } else {
            return (
                <Container className="p-5 mt-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        }
    }
}

export default Logout;
