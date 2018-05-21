package com.game.PlayerManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.game.Entities.PhysicsSprite;
import com.game.GameMain;

public class Player extends PhysicsSprite {
    private final Animation<Texture> haltRight = new Animation<Texture>(.21f,new Texture[]{new Texture("dunlea/idleRight/F0.png"), new Texture("dunlea/idleRight/F1.png")});
    private final Animation<Texture> haltLeft = new Animation<Texture>(.21f,new Texture[]{new Texture("dunlea/idleLeft/F0.png"), new Texture("dunlea/idleLeft/F1.png")});
    private final Animation<Texture> runRight = new Animation<Texture>(.11f,new Texture[]{new Texture("dunlea/moveRight/F0.png"),new Texture("dunlea/moveRight/F1.png"),new Texture("dunlea/moveRight/F2.png")});
    private final Animation<Texture> runLeft = new Animation<Texture>(.11f,new Texture[]{new Texture("dunlea/moveLeft/F0.png"),new Texture("dunlea/moveLeft/F1.png"),new Texture("dunlea/moveLeft/F2.png")});
    public PlayerSensorState state = new PlayerSensorState();

    public Player(World world, float x, float y) {
        super("Player", new Texture("dunlea/idleRight/F0.png"), world, x, y, true);
        new SideSensor(this,Side.Bottem);
    }

    @Override
    protected void InitializeBody(boolean cm){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2f/GameMain.PPM,getHeight()/2f/GameMain.PPM);

        BodyDef bd = new BodyDef();
        bd.position.set((getX()-GameMain.WIDTH/2+getWidth()/2)/GameMain.PPM,(getY()-GameMain.HEIGHT/2+getHeight()/2)/GameMain.PPM);
        bd.fixedRotation = true;
        bd.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bd);

        FixtureDef fd = new FixtureDef();
        fd.density = 1;
        fd.friction = 0;
        fd.shape = shape;
        Fixture fixture = body.createFixture(fd);
        fixture.setUserData(name);
        shape.dispose();
    }

    float elapsed = 0;

    @Override
    public void draw(Batch batch){
        //System.out.println(state.bottem);

        elapsed += Gdx.graphics.getDeltaTime();
        super.draw(batch);
        move();
    }

    boolean wasRight = true;
    private void move(){
        if(Gdx.input.isKeyPressed(Input.Keys.UP) && canJump()){
            super.body.setLinearVelocity(body.getLinearVelocity().x,20);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            super.body.setLinearVelocity(body.getLinearVelocity().x,-10);
        }


        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.RIGHT)){

            if(wasRight) {
                setTexture(haltRight.getKeyFrame(elapsed, true));
            }
            else {
                setTexture(haltLeft.getKeyFrame(elapsed, true));
            }

            body.setLinearVelocity(0,body.getLinearVelocity().y);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            wasRight = true;
            setTexture(runRight.getKeyFrame(elapsed,true));
            body.setLinearVelocity(7,body.getLinearVelocity().y);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            wasRight = false;
            setTexture(runLeft.getKeyFrame(elapsed,true));
            body.setLinearVelocity(-7,body.getLinearVelocity().y);
        }
        else{

            if(wasRight) {
                setTexture(haltRight.getKeyFrame(elapsed, true));
            }
            else {
                setTexture(haltLeft.getKeyFrame(elapsed, true));
            }

            body.setLinearVelocity(0,body.getLinearVelocity().y);
        }
    }

    public World getWorld(){
        return  world;
    }

    private boolean canJump(){
        return body.getLinearVelocity().y<0.001 && body.getLinearVelocity().y>-0.001;
    }
}