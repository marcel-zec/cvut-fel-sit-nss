import React from "react";
import { Container } from "react-bootstrap";
import TripSmall from "./TripSmall";
import CardColumns from "react-bootstrap/CardColumns";
import Spinner from "react-bootstrap/Spinner";
import Cookies from "universal-cookie";

class Index extends React.Component {
    async componentDidMount() {
        const response = await fetch(`http://localhost:8080/trip`, {
            method: "GET",
            mode: "cors",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
        });
        const data = await response.json();
        console.log(data);
        this.setState({ trips: data });
    }
    state = { trips: null };

    render() {
        const cookies = new Cookies();
        console.log("cookieeeeesIndex");
        console.log(cookies.get("JSESSIONID"));
        if (this.state.trips === null) {
            return (
                <Container className="p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            return (
                <Container className="trips">
                    <CardColumns>
                        {this.state.trips.map((trip) => {
                            return (
                                <TripSmall key={trip.short_name} trip={trip} />
                            );
                        })}
                    </CardColumns>
                </Container>
            );
        }
    }
}

export default Index;
