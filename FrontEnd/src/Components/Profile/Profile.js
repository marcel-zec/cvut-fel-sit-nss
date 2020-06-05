import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router-dom";
import ProgressBar from "react-bootstrap/ProgressBar";
import Image from "react-bootstrap/Image";
import { appContext } from "../../appContext";
import Spinner from "react-bootstrap/Spinner";

class Profile extends React.Component {
    static contextType = appContext;
    state = { user: null };
    async componentDidMount() {
        this.setState({ user: this.context.user });
    }

    render() {
        const flexRow = "d-flex justify-content-center";
        const marginTop = "mt-5";
        const flexRowWithMgBtn = flexRow;

        console.log(this.state.user);
        if (this.state.user == null) {
            return (
                <Container className="p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            return (
                <Container id="userProfileMain">
                    <Row className={[flexRowWithMgBtn, marginTop]}>
                        <h3>Brigádnický denníček</h3>
                    </Row>
                    <Row className={flexRowWithMgBtn}>
                        <Col xs={4}>
                            <div className="userPhoto window radius">
                                <Image
                                    src="https://blog.pravda.sk/avatar/blog-1166-256.png"
                                    className="radius"
                                />
                            </div>
                            <div className="userLevel">
                                <h4>
                                    {this.state.user.travel_journal.level}.
                                    level
                                </h4>
                            </div>
                        </Col>
                        <Col>
                            <div className="window radius">
                                <Row>
                                    <Col className="user_info">
                                        <div>
                                            <label>Meno</label>
                                            <h5>
                                                {this.state.user.firstName}{" "}
                                                {this.state.user.lastName}
                                            </h5>
                                        </div>
                                        <div>
                                            <label>Národnosť</label>
                                            <h5>
                                                {
                                                    this.state.user.address
                                                        .country
                                                }
                                            </h5>
                                        </div>
                                    </Col>
                                    <Col className="user_adress">
                                        <div>
                                            <label>Adresa</label>
                                            <p>
                                                {this.state.user.address.street}{" "}
                                                {
                                                    this.state.user.address
                                                        .houseNumber
                                                }
                                            </p>
                                            <p>
                                                {
                                                    this.state.user.address
                                                        .zipCode
                                                }{" "}
                                                {this.state.user.address.city}
                                            </p>
                                        </div>
                                        <div>
                                            <label>Skúsenosť</label>
                                            <h5>
                                                {
                                                    this.state.user
                                                        .travel_journal.xp_count
                                                }{" "}
                                                XP
                                            </h5>
                                        </div>
                                    </Col>
                                </Row>
                                <h5>Progres</h5>
                                <Row>
                                    <Col>
                                        <div className="progressInstance">
                                            <ProgressBar
                                                now={
                                                    ((this.state.user
                                                        .travel_journal
                                                        .xp_count %
                                                        10) *
                                                        100) /
                                                    10
                                                }
                                                label={
                                                    10 -
                                                    (this.state.user
                                                        .travel_journal
                                                        .xp_count %
                                                        10) +
                                                    " XP do ďalšieho levelu"
                                                }
                                            />
                                        </div>
                                    </Col>
                                </Row>
                            </div>
                        </Col>
                    </Row>
                    <Row className="userMenu">
                        <Col>
                            <Link to="/profile/achievments">
                                <FontAwesomeIcon icon="trophy" size="3x" />
                                <h4>Moje achievementy</h4>
                            </Link>
                        </Col>
                        {/*
                        <Col>
                            <Link to="/profile/reviews">
                                <FontAwesomeIcon icon="star" size="3x" />
                                <h4>Moje hodnotenia</h4>
                            </Link>
                        </Col>
                        */}
                        <Col>
                            <Link to="/profile/trips">
                                <FontAwesomeIcon icon="hammer" size="3x" />
                                <h4>Moje brigády</h4>
                            </Link>
                        </Col>

                        <Col>
                            <Link to="/profile/details">
                                <FontAwesomeIcon icon="cog" size="3x" />
                                <h4>Nastavenie</h4>
                            </Link>
                        </Col>
                    </Row>
                </Container>
            );
        }
    }
}

export default Profile;
