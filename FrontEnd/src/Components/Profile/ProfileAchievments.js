import React from "react";
import { Container, Row, Col } from "react-bootstrap";
import AchievmentModal from "../SmartGadgets/AchievementModal";

import Spinner from "react-bootstrap/Spinner";

import { appContext } from "../../appContext";

class ProfileAchievments extends React.Component {
    state = { user: null };
    static contextType = appContext;
    async componentDidMount() {
        this.setState({ user: this.context.user });
    }

    render() {
        console.log("achievments");

        if (this.state.user == null) {
            return (
                <Container className="p-5">
                    <Spinner animation="border" role="status">
                        <span className="sr-only">Loading...</span>
                    </Spinner>
                </Container>
            );
        } else {
            console.log(this.state.user.travel_journal);
            return (
                <Container className="userAchievements">
                    <Row>
                        <div className="window radius fullwidth achievements">
                            <h5>Pracovné</h5>
                            {this.state.user.travel_journal.special.map(
                                (achievement) => (
                                    <div className="achievement">
                                        <div className="circle">
                                            <AchievmentModal
                                                icon={achievement.icon}
                                                title={achievement.name}
                                                description={
                                                    achievement.description
                                                }
                                            />
                                        </div>
                                        <span>{achievement.name}</span>
                                    </div>
                                )
                            )}
                            <h6>
                                Collected{" "}
                                {this.state.user.travel_journal.special.length}
                                /15
                            </h6>
                        </div>
                    </Row>
                    <Row>
                        <div className="window radius fullwidth achievements">
                            <h5>Za brigády v jednej kategórií</h5>
                            {this.state.user.travel_journal.categorized.map(
                                (achievement) => (
                                    <div className="achievement">
                                        <div className="circle">
                                            <AchievmentModal
                                                icon={achievement.icon}
                                                title={achievement.name}
                                                description={
                                                    achievement.description
                                                }
                                            />
                                        </div>
                                        <span>{achievement.name}</span>
                                    </div>
                                )
                            )}
                            <h6>
                                Collected{" "}
                                {
                                    this.state.user.travel_journal.categorized
                                        .length
                                }
                                /8
                            </h6>
                        </div>
                    </Row>
                    <Row>
                        <div className="window radius fullwidth achievements">
                            <h5>Certifikáty</h5>
                            {this.state.user.travel_journal.certificates.map(
                                (achievement) => (
                                    <div className="achievement">
                                        <div className="circle">
                                            <AchievmentModal
                                                icon={achievement.icon}
                                                title={achievement.name}
                                                description={
                                                    achievement.description
                                                }
                                            />
                                        </div>
                                        <span>{achievement.name}</span>
                                    </div>
                                )
                            )}
                            <h6>
                                Collected{" "}
                                {
                                    this.state.user.travel_journal.certificates
                                        .length
                                }
                                /5
                            </h6>
                        </div>
                    </Row>
                </Container>
            );
        }
    }
}
export default ProfileAchievments;
