class RealTimeDisplaysController < ApplicationController
  before_action :set_real_time_display, only: [:show, :edit, :update, :destroy]

  # GET /real_time_displays
  # GET /real_time_displays.json
  def index
    @real_time_displays = RealTimeDisplay.all
  end

  # GET /real_time_displays/1
  # GET /real_time_displays/1.json
  def show
  end

  # GET /real_time_displays/new
  def new
    @real_time_display = RealTimeDisplay.new
  end

  # GET /real_time_displays/1/edit
  def edit
  end

  # POST /real_time_displays
  # POST /real_time_displays.json
  def create
    @real_time_display = RealTimeDisplay.new(real_time_display_params)

    respond_to do |format|
      if @real_time_display.save
        format.html { redirect_to @real_time_display, notice: 'Real time display was successfully created.' }
        format.json { render :show, status: :created, location: @real_time_display }
      else
        format.html { render :new }
        format.json { render json: @real_time_display.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /real_time_displays/1
  # PATCH/PUT /real_time_displays/1.json
  def update
    respond_to do |format|
      if @real_time_display.update(real_time_display_params)
        format.html { redirect_to @real_time_display, notice: 'Real time display was successfully updated.' }
        format.json { render :show, status: :ok, location: @real_time_display }
      else
        format.html { render :edit }
        format.json { render json: @real_time_display.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /real_time_displays/1
  # DELETE /real_time_displays/1.json
  def destroy
    @real_time_display.destroy
    respond_to do |format|
      format.html { redirect_to real_time_displays_url, notice: 'Real time display was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_real_time_display
      @real_time_display = RealTimeDisplay.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def real_time_display_params
      params.fetch(:real_time_display, {})
    end
end
