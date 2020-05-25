import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { Link } from "react-router-dom";
import ProgressBar from "react-bootstrap/ProgressBar";
import Image from "react-bootstrap/Image";
import { appContext } from "../../appContext"
import Spinner from "react-bootstrap/Spinner";

class Profile extends React.Component {
    static contextType = appContext;
    state = {user : null};
    async componentDidMount() {
        this.setState({user:this.context.user});
    }

    render() {
        
        const flexRow = "d-flex justify-content-center";
        const marginTop = "mt-5";
        const flexRowWithMgBtn = flexRow;

        console.log(this.state.user);
        if(this.state.user == null){
            return <Container className="p-5">
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        </Container>;
        }else{
            return (
                <Container id="userProfileMain">
                    <Row className={[flexRowWithMgBtn, marginTop]}>
                        <h3>Your travel Journal</h3>
                    </Row>
                    <Row className={flexRowWithMgBtn}>
                        <Col xs={4}>
                            <div className="userPhoto window radius">
                                <Image src="https://blog.pravda.sk/avatar/blog-1166-256.png" className="radius"/>
                            </div>
                            <div className="userLevel">
                                <h4>2. level</h4>
                            </div>
                        </Col>
                        <Col>
                            <div className="window radius">
                                <Row>
                                    <Col className="user_info">
                                        <div>
                                            <label>Name</label>
                                            <h5>{this.state.user.firstName} {this.state.user.lastName}</h5>
                                        </div>
                                        <div>
                                            <label>Nationality</label>
                                            <h5>{this.state.user.address.country}</h5>
                                        </div>
                                    </Col>
                                    <Col className="user_adress">
                                        <div>
                                            <label>Address</label>
                                            <p>{this.state.user.address.street} {this.state.user.address.houseNumber}</p>
                                            <p>{this.state.user.address.zipCode} {this.state.user.address.city}</p>
                                        </div>
                                        <div>
                                            <label>Registrate from</label>
                                            <p>24.4.2020</p>
                                        </div>
                                    </Col>
                                </Row>
                                <h5>Your progress</h5>
                                <Row>
                                    <Col xs={2}><h6>{(this.state.user.travel_journal.level == 0)? "" : (this.state.user.travel_journal.level)+".Level"}</h6></Col>
                                    <Col><div className="progressInstance"><ProgressBar now={this.state.user.travel_journal.xp_count} label="Just 15 more XP to next level!" /></div></Col>
                                    <Col xs={2}><h6>{(this.state.user.travel_journal.level+1)}.Level</h6></Col>
                                </Row>
                            
                            </div>
                            
                        </Col>
                    </Row>
                    <Row className="userMenu">
                        <Col>
                            <Link to="/profile/details">
                                <FontAwesomeIcon icon="cog" size="3x" />
                                <h4>User settings</h4>
                            </Link>
                        </Col>
                        <Col>
                            <Link to="/profile/achievments">
                                <FontAwesomeIcon icon="trophy" size="3x" />
                                <h4>My achievements</h4>
                            </Link>
                        </Col>
                        <Col>
                            <Link to="/profile/reviews">
                                <FontAwesomeIcon icon="star" size="3x" />
                                <h4>My reviews</h4>
                            </Link>
                        </Col>
                        <Col>
                            <Link to="/profile/trips">
                                <FontAwesomeIcon icon="suitcase" size="3x" />
                                <h4>My trips</h4>
                            </Link>
                        </Col>
                    </Row>
                </Container>
            );
        }
        
    }
}

export default Profile;
