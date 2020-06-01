import React from "react";
import Card from "react-bootstrap/Card";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Button, Row, Col, Container, Image, ListGroup } from "react-bootstrap";
import { Link, withRouter } from "react-router-dom";
import axios from "axios";
import Spinner from "react-bootstrap/Spinner";
import { Form, Modal } from "react-bootstrap";
import AchievmentModal from "../../SmartGadgets/AchievementModal";
import AchievementListInline from "../../SmartGadgets/AchievementListInline";
import { appContext } from "../../../appContext";
import MyAlert from "../../SmartGadgets/MyAlert";
import { BASE_API_URL } from "../../../App";

class Detail extends React.Component {
    state = {
        trip: null,
        selectedSession: {
            id: null,
            from_date: null,
            to_date: null,
            price: null,
        },
    };

    formIsValid = false;
    static contextType = appContext;

    sessionsIds = [];

    async componentDidMount() {
        const response = await fetch(
            BASE_API_URL + "/trip/" + this.props.match.params.id,
            {
                method: "GET",
                mode: "cors",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
            }
        );

        const data = await response.json();
        console.log(data);
        console.log(
            this.context.user ? this.context.user.travel_journal : "neni user"
        );

        this.setState({ trip: data });
        if (data.sessions.length > 0) {
            this.setState({ selectedSession: data.sessions[0] });
        }
        this.sessionsIds = data.sessions.map(function (session) {
            return session.id;
        });
    }
    sessionTripChange = async (selectElement) => {
        const optId = selectElement.value;
        if (this.sessionsIds.includes(parseInt(optId))) {
            //update selectedSession
            const session = this.state.trip.sessions.find(
                (session) => session.id == optId
            );
            this.setSelectedSession(session);
        }
    };
    setSelectedSession(session) {
        this.setState({ selectedSession: session });
    }
    submitTrip(event, formElement) {
        event.preventDefault();
        console.log(this.state.selectedSession);
        //check checbox is checked
        let checboxIsChecked = false;
        checboxIsChecked = document.querySelector("#checkboxAgreement input")
            .checked;
        if (checboxIsChecked && this.formIsValid) {
            console.log("READY K ODESLANI");
            fetch(BASE_API_URL + "/trip/" + this.state.trip.short_name, {
                method: "POST",
                mode: "cors",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(this.state.selectedSession),
            }).then((response) => {
                if (response.ok) {
                    window.setTimeout(function () {
                        alert("Trip was added to your travel journal");
                        document.location.reload();
                    }, 500);
                }
                /*this.props.history.push({
                        pathname: "/",
                        alert: (
                            <MyAlert
                                heading="Trip was added to your travel journal"
                                text="Thank you, now please pay the deposit in your user profile"
                                flash={true}
                            />
                        ),
                    });*/
                //TODO - osetrenie vynimiek
                else alert("Error: somethhing goes wrong");
            });
        }
    }

    validateUserAchievement(achievement, userList) {
        return userList.some((item) => item.id == achievement.id);
    }

    /**
     * Validation before purchase trip.
     * @param {event} event
     */
    validatePurchase(event) {
        event.preventDefault();
        document.querySelector(".popup_background").classList.remove("hidden");
        let specialValidate = false;
        let certificateslValidate = false;
        let categorizedValidate = false;
        let levelPassed = false;

        const req_ach_special = this.state.trip.required_achievements_special;
        const req_ach_categorized = this.state.trip
            .required_achievements_categorized;
        const req_cerf = this.state.trip.required_achievements_certificate;
        const minlevel = this.state.trip.required_level;

        specialValidate = req_ach_special.every((val) =>
            this.validateUserAchievement(
                val,
                this.context.user
                    ? this.context.user.travel_journal.special
                    : []
            )
        );
        categorizedValidate = req_ach_categorized.every((val) =>
            this.validateUserAchievement(
                val,
                this.context.user
                    ? this.context.user.travel_journal.categorized
                    : []
            )
        );
        certificateslValidate = req_cerf.every((val) =>
            this.validateUserAchievement(
                val,
                this.context.user
                    ? this.context.user.travel_journal.certificates
                    : []
            )
        );
        levelPassed =
            (this.context.user
                ? Number(this.context.user.travel_journal.level)
                : 0) >= minlevel;

        if (
            specialValidate &&
            categorizedValidate &&
            certificateslValidate &&
            levelPassed
        ) {
            console.log("je to validdni");
            this.formIsValid = true;
        } else {
            console.log("neni to validni");
            document.querySelector("#confirmPurchase").style.display = "none";
            document.querySelector("#checkboxAgreement").style.display = "none";
            document.querySelector("#validationFalse").style.display = "block";
        }
    }
    closeValidateWindow(element) {
        document.querySelector(".popup_background").classList.add("hidden");
    }

    /**
     * Render rating stars.
     * @param {Number} rating
     */
    renderRating(rating) {
        let starsElement = [];
        if (rating == 0) {
            return null;
        }
        for (var i = 1; i <= rating; i++) {
            starsElement.push(<FontAwesomeIcon key={i} icon="star" />);
        }
        if (rating - starsElement.length >= 0.5) {
            starsElement.push(
                <FontAwesomeIcon
                    key={starsElement.length + 1}
                    icon="star-half"
                />
            );
        }
        return starsElement;
    }

    /**
     * Return check-circle icon or minus-circle icons after trip level and user level validation.
     */
    validLevel() {
        if (
            (this.context.user
                ? Number(this.context.user.travel_journal.level)
                : 0) >= this.state.trip.required_level
        ) {
            return <FontAwesomeIcon className="checked" icon="check-circle" />;
        }
        return <FontAwesomeIcon className="false" icon="minus-circle" />;
    }

    /**
     * Render achievements.
     * @param {*} achievements
     * @param {*} message
     */
    renderAchievements(achievements, message = "No achievements are required") {
        if (achievements.length == 0) {
            return message;
        }
        let toReturn = [];
        achievements.forEach((element) => {
            toReturn.push(
                <ListGroup.Item>
                    <AchievmentModal
                        titleBeforeIcon={true}
                        icon={element.icon}
                        title={element.name}
                        description={element.description}
                    />
                </ListGroup.Item>
            );
        });
        return toReturn;
    }

    dateTimeFormater(dateToFormat) {
        const date = new Date(dateToFormat);
        let formated = "";
        formated +=
            date.getDate() +
            "." +
            (date.getMonth() + 1) +
            "." +
            date.getFullYear();
        return formated;
    }

    render() {
        if (this.state.trip === null) {
            return (
                <Container className="p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            //setting sessions trip
            let options = null;
            let sessionBlock = null;
            let dateTitle = "Dátum";

            //create list of options for select when more sessions(dates)
            let optionArray = [];
            this.state.trip.sessions.forEach((element) => {
                optionArray.push(
                    <option key={element.id} value={element.id}>
                        {element.from_date + " " + element.to_date}
                    </option>
                );
            });
            //if trip does not have any session show it
            if (this.state.trip.sessions == 0) {
                options = <div>Trip nemá žádnou session</div>;
                sessionBlock = (
                    <div className="trip_price">Nemôžeš sa zapísať</div>
                );
            } else if (this.context.user == null) {
                options = (
                    <Form.Control
                        as="select"
                        id="dateSessionSelect"
                        onChange={(event) =>
                            this.sessionTripChange(event.target)
                        }
                    >
                        {optionArray}
                    </Form.Control>
                );
                sessionBlock = (
                    <div>
                        <div className="trip_price">
                            <span id="tripPrice">
                                {" "}
                                {this.state.trip.salary}
                            </span>
                            Kč
                        </div>
                        <div>
                            Zapísať sa môžu iba prihlásený uživatelia.{" "}
                            <Link to="/login">Login</Link>
                        </div>
                    </div>
                );
            } else {
                options = (
                    <Form.Control
                        as="select"
                        id="dateSessionSelect"
                        onChange={(event) =>
                            this.sessionTripChange(event.target)
                        }
                    >
                        {optionArray}
                    </Form.Control>
                );
                sessionBlock = (
                    <div>
                        <div className="trip_price">
                            <span id="tripPrice">
                                {" "}
                                {this.state.trip.salary}
                            </span>
                            Kč
                        </div>
                        <Button
                            className="submit"
                            variant="primary"
                            type="submit"
                            onClick={(event) => this.validatePurchase(event)}
                        >
                            {" "}
                            Zapísať sa{" "}
                        </Button>
                    </div>
                );
            }
            //set correct date(s)
            if (this.state.trip.sessions.length > 1) {
                dateTitle = "Dátumy";
            }
            //setting reviews
            const reviews = this.state.trip.jobReviewDtos;
            const reviewsBlock = reviews.map((review) => (
                <div className="review">
                    <Row>
                        <Col className="rev_author" xs={6}>
                            <FontAwesomeIcon icon="user-alt" />
                            <span>{review.author}</span>
                            <span className="text-muted">
                                {this.dateTimeFormater(review.date)}
                            </span>
                        </Col>
                        <Col className="rev_rating" xs={6}>
                            {this.renderRating(review.rating)}
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <p className="note">{review.note}</p>
                        </Col>
                    </Row>
                </div>
            ));
            if (reviewsBlock.length == 0) {
                reviewsBlock.push(
                    <Row className="d-flex justify-content-center">
                        Brigáda zatiaľ nemá žiadne hodnotenie.
                    </Row>
                );
            }
            //render page
            return (
                <Container id="trip_detail">
                    <Card className="mb-3 trip_main">
                        <Card.Body className="d-flex flex-row">
                            <Col xs={5} className="image">
                                <Image
                                    src="https://specials-images.forbesimg.com/imageserve/5db15891616a45000704761f/960x0.jpg?fit=scale"
                                    rounded
                                />
                            </Col>
                            <Col xs={7} className="trip_info">
                                <Row className="d-flex flex-column">
                                    <Col>
                                        <Card.Title className="trip_name">
                                            {this.state.trip.name}
                                        </Card.Title>
                                    </Col>
                                </Row>
                                <Form>
                                    <Row>
                                        <Col>
                                            <Card.Title className="mb-2 text-muted">
                                                <FontAwesomeIcon icon="map-marker-alt" />{" "}
                                                Miesto
                                            </Card.Title>
                                            <Card.Text>
                                                {this.state.trip.location}
                                            </Card.Text>
                                            <Card.Title className="mb-2 text-muted">
                                                <FontAwesomeIcon icon="map-signs" />{" "}
                                                Získaš skúsenosti
                                            </Card.Title>
                                            <Card.Text>
                                                {
                                                    this.state.trip
                                                        .possible_xp_reward
                                                }{" "}
                                                XP
                                            </Card.Text>
                                            <Card.Title className="mb-2 text-muted">
                                                <FontAwesomeIcon icon="calendar-alt" />{" "}
                                                {dateTitle}
                                            </Card.Title>
                                            {options}
                                        </Col>
                                        <Col>
                                            <div className="rev-price-buy">
                                                <div className="review_element">
                                                    {this.renderRating(
                                                        this.state.trip.rating
                                                    )}
                                                </div>
                                                {sessionBlock}
                                            </div>
                                        </Col>
                                    </Row>
                                </Form>
                            </Col>
                        </Card.Body>
                    </Card>
                    <Row>
                        <Col className="col-4 trip_restriction">
                            <Card className="mb-3">
                                <Card.Body>
                                    <Card.Subtitle className="mb-2 text-muted">
                                        <FontAwesomeIcon icon="money-bill" />{" "}
                                        Výplata
                                    </Card.Subtitle>
                                    <Card.Text>
                                        {this.state.trip.salary} Kč / hod
                                    </Card.Text>
                                    <Card.Subtitle className="mb-2 text-muted">
                                        {this.validLevel()}
                                        Požadovaný minimálny level
                                    </Card.Subtitle>
                                    <Card.Text>
                                        {this.state.trip.required_level}
                                    </Card.Text>
                                </Card.Body>
                            </Card>
                            <Card className="mb-3">
                                <Card.Body>
                                    <Card.Title>
                                        Požadované certifikáty
                                    </Card.Title>
                                    <ListGroup variant="flush">
                                        {this.renderAchievements(
                                            this.state.trip
                                                .required_achievements_certificate,
                                            "Certifikáty nie sú požadované"
                                        )}
                                    </ListGroup>
                                </Card.Body>
                            </Card>
                            <Card className="mb-3">
                                <Card.Body>
                                    <Card.Title>
                                        Požadované achievementy
                                    </Card.Title>
                                    <ListGroup variant="flush">
                                        {this.renderAchievements(
                                            this.state.trip
                                                .required_achievements_special,
                                            "Brigádnické achievementy nie sú požadované"
                                        )}
                                    </ListGroup>
                                    <Card.Subtitle className="subtitle">
                                        Za brigády v kategórií:
                                    </Card.Subtitle>
                                    <ListGroup variant="flush">
                                        {this.renderAchievements(
                                            this.state.trip
                                                .required_achievements_categorized
                                        )}
                                    </ListGroup>
                                </Card.Body>
                            </Card>
                            <Card className="mb-3">
                                <Card.Body>
                                    <Card.Title>
                                        Môžeš získať achievementy
                                    </Card.Title>
                                    <ListGroup variant="flush">
                                        {this.renderAchievements(
                                            this.state.trip
                                                .gain_achievements_special,
                                            "Nemôžeš získať žiaden achievement"
                                        )}
                                    </ListGroup>
                                </Card.Body>
                            </Card>
                        </Col>
                        <Col className="col-8">
                            <Card className="mb-5">
                                <Card.Body>
                                    <Card.Title>Popis brigády</Card.Title>
                                    <Card.Text>
                                        {this.state.trip.description}
                                    </Card.Text>
                                </Card.Body>
                            </Card>
                            <Card
                                id="trip_reviews"
                                className="mb-5 trip_reviews"
                            >
                                <Card.Body>
                                    <Card.Title>Hodnotenia</Card.Title>
                                    {reviewsBlock}
                                </Card.Body>
                            </Card>
                        </Col>
                    </Row>
                    <div className="popup_background hidden">
                        <div className="window radius trip_validation customScroll">
                            <span
                                className="close"
                                onClick={(event) =>
                                    this.closeValidateWindow(event.target)
                                }
                            >
                                <FontAwesomeIcon icon="times"></FontAwesomeIcon>
                            </span>
                            <h5>Súhrn brigády</h5>
                            <Row>
                                <Col className="alignLeft">
                                    <Card.Title>Meno</Card.Title>
                                    <Card.Body>
                                        {this.state.trip.name}
                                    </Card.Body>
                                </Col>
                            </Row>
                            <Row>
                                <Col className="alignLeft">
                                    <Card.Title>Miesto</Card.Title>
                                    <Card.Body>
                                        {this.state.trip.location}
                                    </Card.Body>
                                </Col>
                                <Col>
                                    <Card.Title>Odmena</Card.Title>
                                    <Card.Body>
                                        {this.state.trip.possible_xp_reward} XP
                                    </Card.Body>
                                </Col>
                            </Row>
                            <Row>
                                <Col xs={6} className="alignLeft">
                                    <Card.Title>Dátum</Card.Title>
                                    <Card.Body>
                                        {this.state.selectedSession.from_date +
                                            " až " +
                                            this.state.selectedSession.to_date}
                                    </Card.Body>
                                </Col>

                                <Col>
                                    <Card.Title>Výplata</Card.Title>
                                    <Card.Body>
                                        {this.state.trip.salary} Kč / hod
                                    </Card.Body>
                                </Col>
                            </Row>
                            <div className="achievements">
                                <h5>Požiadávky</h5>
                                <Row>
                                    <Col className="alignLeft">
                                        <Card.Title>
                                            Požadované certifikáty
                                        </Card.Title>
                                        <Card.Body className="flex">
                                            <AchievementListInline
                                                achievements={
                                                    this.state.trip
                                                        .required_achievements_certificate
                                                }
                                                userList={
                                                    this.context.user
                                                        ? this.context.user
                                                              .travel_journal
                                                              .certificates
                                                        : []
                                                }
                                                message={
                                                    "Certifikáty nie sú požadované"
                                                }
                                            />
                                        </Card.Body>
                                    </Col>
                                    <Col xs={4}>
                                        <Card.Title>Minimálny level</Card.Title>
                                        <Card.Body>
                                            <span
                                                style={{
                                                    display: "inline-block",
                                                    verticalAlign: "middle",
                                                    marginRight: "10px",
                                                    marginTop: "-5px",
                                                }}
                                            >
                                                {this.state.trip.required_level}
                                            </span>{" "}
                                            {this.validLevel()}
                                        </Card.Body>
                                    </Col>
                                </Row>
                                <Row></Row>
                                <Row>
                                    <Col className="alignLeft">
                                        <Card.Title>
                                            Požadované achievementy
                                        </Card.Title>

                                        <Card.Body>
                                            <AchievementListInline
                                                achievements={
                                                    this.state.trip
                                                        .required_achievements_special
                                                }
                                                userList={
                                                    this.context.user
                                                        ? this.context.user
                                                              .travel_journal
                                                              .special
                                                        : []
                                                }
                                                message={
                                                    "Brigádnícké achievementy nie sú požadované"
                                                }
                                            />
                                        </Card.Body>
                                        <Card.Body>
                                            <AchievementListInline
                                                achievements={
                                                    this.state.trip
                                                        .required_achievements_categorized
                                                }
                                                userList={
                                                    this.context.user
                                                        ? this.context.user
                                                              .travel_journal
                                                              .categorized
                                                        : []
                                                }
                                                message={
                                                    "Achievementy za brigády v kategórií nie sú požadované"
                                                }
                                            />
                                        </Card.Body>
                                    </Col>
                                </Row>
                                <form
                                    onSubmit={(event) =>
                                        this.submitTrip(event, event.target)
                                    }
                                >
                                    <Row>
                                        <Col className="alignLeft">
                                            <label
                                                id="checkboxAgreement"
                                                className="containerInput"
                                            >
                                                Súhlasím so spracovaním osobných
                                                údajov v súlade s pravidlami
                                                GDPR a pravidlami spoločnosti
                                                Part-time John, s.r.o
                                                <input type="checkbox" />
                                                <span className="checkmark"></span>
                                                <div className="validate_error">
                                                    Musíš odsúhlasiť podmienky!
                                                </div>
                                            </label>
                                            <div id="validationFalse">
                                                Je nám to ľúto ale nesplnáš
                                                podmienky aby si sa mohol
                                                zapísať na brigádu.
                                            </div>
                                        </Col>
                                    </Row>
                                    <Row>
                                        <Col></Col>
                                        <Col xs={6}>
                                            <Button
                                                id="confirmPurchase"
                                                className="submit"
                                                variant="primary"
                                                type="submit"
                                            >
                                                Potvrdiť zápis
                                            </Button>
                                        </Col>
                                        <Col></Col>
                                    </Row>
                                </form>
                            </div>
                        </div>
                    </div>
                </Container>
            );
        }
    }
}

export default withRouter(Detail);
