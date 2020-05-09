import React from "react";
import { Container, Card, Form, Row, Col, Button } from "react-bootstrap";
import TripSmall from "./TripSmall";
import CardColumns from "react-bootstrap/CardColumns";
import Spinner from "react-bootstrap/Spinner";
import Cookies from "universal-cookie";
import DatePicker from "react-datepicker";
import RangeSlider from "react-bootstrap-range-slider";
import "react-bootstrap-range-slider/dist/react-bootstrap-range-slider.css";
import TripMedium from "./TripMedium";

class IndexFilter extends React.Component {
    state = {
        trips: null,
        filter: {
            from: null,
            to: null,
            price: 6000,
            words: [],
        },
    };

    componentDidUpdate(prevState) {
        if (prevState.location.search != this.props.location.search) {
            const newState = { ...this.state };
            newState.filter["words"] = this.searchParameter();
            this.setState({ state: newState });
            this.fetchData();
        }
    }

    searchParameter = () => {
        const urlParams = new URLSearchParams(this.props.location.search);
        const parameters = urlParams.get("search");
        return parameters ? parameters.split(" ") : null;
    };

    inputUpdateHandler = (event, inputName) => {
        const newState = { ...this.state.filter };
        if (inputName == "price")
            newState["price"] = Number(event.target.value);
        else if (inputName == "from" || inputName == "to") {
            let newDate = new Date(event);
            console.log("date " + inputName);
            console.log(newDate);
            newDate.setTime(
                newDate.getTime() - new Date().getTimezoneOffset() * 60 * 1000
            );
            newState[inputName] = newDate;
        }
        this.setState({ filter: newState });
    };

    searchUrl = () => {
        let filterUrl = "http://localhost:8080/trip/filter?";
        let filter = false;

        const searchParameters = this.searchParameter();
        console.log("searching");
        console.log(searchParameters);
        console.log(this.state.filter.price);

        if (searchParameters && searchParameters.length > 0) {
            searchParameters.forEach((param) => {
                if (filter) {
                    filterUrl += "&search=" + param;
                } else {
                    filterUrl += "search=" + param;
                }

                filter = true;
            });
        }
        if (this.state.filter.price) {
            if (filter) {
                filterUrl += "&max_price=" + this.state.filter.price;
            } else {
                filterUrl += "max_price=" + this.state.filter.price;
            }
            filter = true;
        }
        return filter ? filterUrl : "http://localhost:8080/trip";
    };

    fetchData = async (event = null) => {
        if (event) {
            event.preventDefault();
        }
        const url = this.searchUrl();
        console.log(url);
        await fetch(url, {
            method: "GET",
            mode: "cors",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                console.log(response);
                if (response.ok) return response.json();
                else console.error(response.status);
            })
            .then((data) => {
                console.log(data);
                this.setState({ trips: data });
            })
            .catch((error) => {
                console.error(error);
            });
    };

    async componentDidMount() {
        const newState = { ...this.state };
        newState.filter.words = this.searchParameter();
        if (newState.filter.words != this.state.filter.words) {
            this.setState(newState);
        }

        this.fetchData();
    }

    render() {
        if (this.state.trips == null) {
            return (
                <Container className="p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            return (
                <Container className="d-flex mt-3">
                    <Col>
                        <Card>
                            <Form
                                className="p-3"
                                onSubmit={(event) => this.fetchData(event)}
                            >
                                <Form.Group>
                                    <Form.Label>Date</Form.Label>
                                    <Card.Body className="d-flex">
                                        <Form.Label>from </Form.Label>
                                        <DatePicker
                                            className="form-control"
                                            dateFormat="dd. MM. yyyy"
                                            selected={this.state.filter.from}
                                            onChange={(event) =>
                                                this.inputUpdateHandler(
                                                    event,
                                                    "from"
                                                )
                                            }
                                        />
                                        <Form.Label>to </Form.Label>
                                        <DatePicker
                                            className="form-control ml-3"
                                            dateFormat="dd. MM. yyyy"
                                            selected={this.state.filter.to}
                                            onChange={(event) =>
                                                this.inputUpdateHandler(
                                                    event,
                                                    "to"
                                                )
                                            }
                                        />
                                    </Card.Body>
                                </Form.Group>
                                <Form.Group>
                                    <Card.Body className="d-flex">
                                        <Form.Label>Price </Form.Label>
                                        <RangeSlider
                                            value={this.state.filter.price}
                                            min={0}
                                            max={6000}
                                            step={100}
                                            tooltip="auto"
                                            tooltipPlacement="top"
                                            onChange={(event) =>
                                                this.inputUpdateHandler(
                                                    event,
                                                    "price"
                                                )
                                            }
                                        />
                                    </Card.Body>
                                </Form.Group>
                                <Button variant="primary" type="submit">
                                    Filter
                                </Button>
                            </Form>
                        </Card>
                    </Col>

                    <Col className="ml-3">
                        {/*<CardColumns className="d-flex flex-column">*/}
                        {this.state.trips.map((trip) => {
                            return (
                                <Row>
                                    <TripMedium
                                        key={trip.short_name}
                                        highlightWords={this.state.filter.words}
                                        trip={trip}
                                    />
                                </Row>
                            );
                        })}
                        {/*</CardColumns>*/}
                    </Col>

                    {/*<CardColumns>
                        {this.state.trips.map((trip) => {
                            return (
                                <TripSmall key={trip.short_name} trip={trip} />
                            );
                        })}
                    </CardColumns>*/}
                </Container>
            );
        }
    }
}

export default IndexFilter;
